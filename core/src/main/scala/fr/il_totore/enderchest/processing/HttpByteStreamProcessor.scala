package fr.il_totore.enderchest.processing

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, HttpResponse}
import akka.http.scaladsl.server.StandardRoute
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

trait HttpByteStreamProcessor extends HttpProcessor {
  override def process(request: HttpRequest)(implicit materializer: Materializer, context: ExecutionContext): StandardRoute = {
    request.entity match {
      case strict: HttpEntity.Strict =>
        val entityFut = request.entity.toStrict(FiniteDuration(60, TimeUnit.SECONDS))
        val byteStringFut = entityFut.flatMap(entity => {
          entity.dataBytes.runFold(ByteString.empty)(_ ++ _)
        })

        complete(byteStringFut.map(byteString => new ByteArrayInputStream(byteString.toArray))
          .map(processStream)
          .map(output => Source.single(ByteString(output.toByteArray)))
          .map(src => HttpResponse(entity = HttpEntity.Chunked.fromData(ContentTypes.`application/octet-stream`, src))))


      case _ => complete("Request is not strict")
    }
  }

  def processStream(input: ByteArrayInputStream): ByteArrayOutputStream

}
