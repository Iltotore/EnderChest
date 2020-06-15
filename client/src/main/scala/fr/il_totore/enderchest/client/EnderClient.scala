package fr.il_totore.enderchest.client

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest}
import akka.http.scaladsl.{Http, HttpExt}
import akka.stream.{ActorMaterializer, Materializer}
import fr.il_totore.enderchest.io.FileAnalyzer
import fr.il_totore.enderchest.io.FileChecksum.Protocol._
import spray.json._

import scala.concurrent.{ExecutionContextExecutor, Future}

class EnderClient(address: String, fileAnalyzer: FileAnalyzer)
                 (implicit actor: ActorSystem, context: ExecutionContextExecutor, materializer: Materializer) {


  private implicit val http: HttpExt = Http(actor)

  def checkFiles: Future[Int] = fileAnalyzer.check

  def update: Future[Done] = {
    val array = JsArray((for (checksum <- fileAnalyzer.getChecksums) yield checksum.toJson).toVector)
    val entity = HttpEntity(ContentTypes.`application/json`, array.compactPrint)
    val request = HttpRequest(uri = address, entity = entity)
    http.singleRequest(request)
      .flatMap(response => response.entity match {
        case HttpEntity.Chunked(_, chunks) =>
          chunks
            .filterNot(_.isLastChunk)
            .map(_.data())
            .runFold(new ChunkedDownloader(fileAnalyzer.getDirectory))((downloader, data) =>
              downloader.process(data))
            .map(_.close())

        case _ => throw new IllegalStateException("Received wrong response: " + response)
      })
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