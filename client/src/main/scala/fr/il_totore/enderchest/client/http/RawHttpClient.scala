package fr.il_totore.enderchest.client.http

import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.concurrent.Future

class RawHttpClient(implicit base: HttpExt) extends HttpClient[HttpRequest, HttpResponse] {
  override def send(query: HttpRequest): Future[HttpResponse] = base.singleRequest(query)
}
