package fr.il_totore.enderchest.client.http

import scala.concurrent.Future

abstract class HttpClient[Q, R] {

  def send(query: Q): Future[R]
}
