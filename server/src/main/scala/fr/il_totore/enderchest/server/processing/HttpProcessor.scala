package fr.il_totore.enderchest.server.processing

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.StandardRoute
import akka.stream.Materializer

import scala.concurrent.ExecutionContext

trait HttpProcessor {

  def process(request: HttpRequest)(implicit materializer: Materializer, context: ExecutionContext): StandardRoute
}
