package fr.il_totore.enderchest.server

import java.io.File
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.Materializer
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import fr.il_totore.enderchest.io.EndLogger._
import fr.il_totore.enderchest.io.{FileAnalyzer, FileChecksum}
import org.simpleyaml.configuration.file.YamlConfiguration
import spray.json.{JsNumber, JsObject, JsString, _}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContextExecutor, Future}

class Server(args: Array[String], configFile: File)(implicit system: ActorSystem, materializer: Materializer, contextExecutor: ExecutionContextExecutor) {

  var analyzer: FileAnalyzer = _
  val http = Http()

  def start(): Unit = {
    if (!configFile.exists()) Files.copy(getClass.getResourceAsStream("/config.yml"), configFile.toPath)
    val config = YamlConfiguration.loadConfiguration(configFile)
    val dir = new File(config.getString("file.directory"))
    if (!dir.exists()) dir.mkdirs()
    val pattern = Pattern.compile(config.getString("file.exclude")).asPredicate()
    val exclude = pattern.test _
    analyzer = new FileAnalyzer(dir, exclude, config.getBoolean("file.recursive"))

    http.bindAndHandleAsync(request => {
      request.entity.toStrict(FiniteDuration(1, TimeUnit.MINUTES)).map(strict => receiveUpdatePart(strict.data.utf8String.parseJson.asInstanceOf[JsArray]))
    }, config.getString("network.ip"), config.getInt("network.port"))
    info(s"Listening ${config.getString("network.ip")}:${config.getInt("network.port")}")
  }

  def receiveUpdatePart(array: JsArray): HttpResponse = {
    val receivedChecksums = new ArrayBuffer[FileChecksum]()
    for (value <- array.elements if value.isInstanceOf[JsObject]) {
      processChecksum(value.asJsObject).foreach(receivedChecksums.addOne)
    }

    val source = Source(analyzer.getChecksums.toVector)
      .filterNot(receivedChecksums.contains)
      .flatMapConcat(checksum => FileIO.fromPath(new File(analyzer.getDirectory, checksum.relativePath).toPath)
        .prepend(Source(Vector(
          ByteString("ENDERCHEST_FILE_SEPARATOR"),
          ByteString(checksum.relativePath)
        ))))


    info(s"Sending flow...")
    HttpResponse(entity = HttpEntity(ContentTypes.`application/octet-stream`, source))
  }

  def stop(): Future[Terminated] = system.terminate()

  def processChecksum(json: JsObject): Option[FileChecksum] = {
    json.getFields("path", "hash") match {
      case Seq(JsString(path), JsNumber(hash)) => Option(new FileChecksum(path, hash.toIntExact))

      case _ => Option.empty
    }
  }
}