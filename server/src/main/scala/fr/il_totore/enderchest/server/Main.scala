package fr.il_totore.enderchest.server

import java.io.DataInputStream

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import fr.il_totore.enderchest.server.handler.UpdateProcessor
import fr.il_totore.enderchest.server.info.{InMessage, UpdateInMessage}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: Materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val inRegistry: List[(Byte, DataInputStream) => InMessage] = List(
      (id, stream) => new UpdateInMessage(id, stream)
    )

    val handler = new UpdateProcessor

    val route =
      path("update") {
        extractRequest(handler.process)
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
