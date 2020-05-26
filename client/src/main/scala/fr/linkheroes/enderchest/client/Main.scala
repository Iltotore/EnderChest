package fr.linkheroes.enderchest.client

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{extractRequest, path}
;

object Main {

  def main(args: Array[String]): Unit = {
    println("Enderchest -> Ready to use !")
  }

  def connect (string: String, int: Int): Unit = {
    val handler = new UpdateProcessor

    val route =
      path("update") {
        extractRequest(handler.process)
      }

    val connect = Http().bindAndHandle(route, String, Int)
  }
}
