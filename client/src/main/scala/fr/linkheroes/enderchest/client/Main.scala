package fr.linkheroes.enderchest.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{ Failure, Success }
object Main {

  def main(args: Array[String]): Unit = {
    println("[Enderchest] -> Ready to use !")

    connect("http://medievalia.jdteam.fr", 8080)

  }

  def connect (host: String, port: Int): Unit = {
    println("[EnderChest] Connect to " + host + ":" + port)

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = host))

    responseFuture
      .onComplete {
        case Success(res) => println(res)
        case Failure(_)   => sys.error("something wrong")
      }

  }
}
