package fr.il_totore.enderchest.client

import java.io.DataInputStream

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import fr.il_totore.enderchest.client.http.{HttpByteStreamClient, MessageHttpClient, RawHttpClient}
import fr.il_totore.enderchest.io.FileAnalyzer
import fr.il_totore.enderchest.messaging.InMessage

import scala.concurrent.ExecutionContextExecutor

class EnderClient(uri: String, fileAnalyzer: FileAnalyzer)
                 (implicit context: ExecutionContextExecutor, materializer: Materializer, registry: List[DataInputStream => InMessage]) {

  private var httpClient = new MessageHttpClient(new HttpByteStreamClient(new RawHttpClient(Http(ActorSystem("enderchest")))))

  def checkFiles(): Unit = {
    fileAnalyzer.check()
  }

  def bind(): Unit = {
    Http(uri)
  }
}
