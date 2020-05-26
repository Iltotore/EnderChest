package fr.il_totore.enderchest.client.http

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, HttpResponse}
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.concurrent.{ExecutionContextExecutor, Future}


class HttpByteStreamClient(baseClient: HttpClient[HttpRequest, HttpResponse], uri: String)(implicit context: ExecutionContextExecutor, materializer: Materializer)
  extends ProcessedHttpClient[ByteArrayOutputStream, ByteArrayInputStream](uri) {

  override def send(query: ByteArrayOutputStream): Future[ByteArrayInputStream] = {
    val source = Source.single(ByteString(query.toByteArray))
    val entity = HttpEntity.Chunked.fromData(ContentTypes.`application/octet-stream`, source)
    baseClient.send(Get(uri, entity))
      .flatMap(response => response.entity.dataBytes.runFold(ByteString.empty)(_ ++ _))
      .map(byteString => new ByteArrayInputStream(byteString.toArray))
  }
}
