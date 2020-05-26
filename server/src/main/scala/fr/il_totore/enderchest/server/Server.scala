package fr.il_totore.enderchest.server

import java.io.{DataInputStream, File}
import java.nio.file.Files

import akka.actor.ActorSystem
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.Materializer
import fr.il_totore.enderchest.messaging.InMessage

import scala.concurrent.{ExecutionContextExecutor, Future}

class Server(args: Array[String], configFile: File)(implicit system: ActorSystem, materializer: Materializer, contextExecutor: ExecutionContextExecutor, inRegistry: List[(Byte, DataInputStream) => InMessage]) {

  var bindingFuture: Future[ServerBinding] = _

  def start(): Unit = {
    if (!configFile.exists()) Files.copy(getClass.getResourceAsStream("/config.yml"), configFile.toPath)
  }

  def bind(): Unit = {

  }

  def stop(): Unit = {
    bindingFuture.flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
