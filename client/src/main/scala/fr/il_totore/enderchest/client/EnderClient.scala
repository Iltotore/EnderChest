package fr.il_totore.enderchest.client

import java.io.{ByteArrayInputStream, File, FileOutputStream}
import java.util.Base64
import java.util.zip.GZIPInputStream

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest}
import akka.http.scaladsl.{Http, HttpExt}
import akka.stream.{ActorMaterializer, Materializer}
import fr.il_totore.enderchest.io.FileAnalyzer
import fr.il_totore.enderchest.io.FileChecksum.Protocol._
import org.apache.commons.io.IOUtils
import spray.json._

import scala.concurrent.{ExecutionContextExecutor, Future}

class EnderClient(address: String, fileAnalyzer: FileAnalyzer)
                 (implicit actor: ActorSystem, context: ExecutionContextExecutor, materializer: Materializer) {


  private implicit val http: HttpExt = Http(actor)

  def checkFiles(): Future[Done] = fileAnalyzer.check

  def update(): Future[Done] = {
    val array = JsArray((for (checksum <- fileAnalyzer.getChecksums) yield checksum.toJson).toVector)
    val entity = HttpEntity(ContentTypes.`application/json`, array.compactPrint)
    val request = HttpRequest(uri = address + "/update", entity = entity)
    http.singleRequest(request)
      .flatMap(_.entity match {
        case HttpEntity.Chunked(_, chunks) => chunks.filterNot(_.isLastChunk)
          .runForeach(chunk => downloadFromJson(chunk.data().utf8String.parseJson.asJsObject))

        case _ => throw new IllegalArgumentException("Only able to deserialize chunked files")
      })
  }

  def downloadFromJson(obj: JsObject): Unit = {
    obj.getFields("path", "data") match {
      case Seq(JsString(path), JsString(data)) =>
        println("Downloading " + path)
        val file = new File(fileAnalyzer.getDirectory, path)
        if (!file.getParentFile.exists()) file.getParentFile.mkdirs()
        if (!file.exists()) file.createNewFile()
        val outStream = new FileOutputStream(file)
        val byteStream = new ByteArrayInputStream(Base64.getDecoder.decode(data))
        val gzipStream = new GZIPInputStream(byteStream)
        IOUtils.copy(gzipStream, outStream)
        outStream.close()

      case _ =>
    }
  }
}

object EnderClient {

  val DEFAULT_SYSTEM: ActorSystem = ActorSystem("enderchest")
  val DEFAULT_EXECUTION_CONTEXT: ExecutionContextExecutor = DEFAULT_SYSTEM.dispatcher
  val DEFAULT_MATERIALIZER: Materializer = ActorMaterializer()(DEFAULT_SYSTEM)

  def apply(address: String, fileAnalyzer: FileAnalyzer,
            actorSystem: ActorSystem = DEFAULT_SYSTEM,
            context: ExecutionContextExecutor = DEFAULT_EXECUTION_CONTEXT,
            materializer: Materializer = DEFAULT_MATERIALIZER): EnderClient =
    new EnderClient(address, fileAnalyzer)(actorSystem, context, materializer)
}