package fr.il_totore.enderchest.client

import java.io.DataInputStream

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import fr.il_totore.enderchest.messaging.InMessage

import scala.concurrent.ExecutionContextExecutor

object Main {

  def main(args: Array[String]): Unit = {
    println("[Enderchest] -> Ready to use !")
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: Materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val inRegistry: List[(Byte, DataInputStream) => InMessage] = List(
      (id, stream) => new ErrorMessage(id, stream)
    )
  }

  def connect(host: String): Unit = {
    println("[EnderChest] Connect to " + host)
  }
}
