package fr.il_totore.enderchest.client

import java.io.File

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import fr.il_totore.enderchest.io.EndLogger._
import fr.il_totore.enderchest.io.FileAnalyzer

import scala.concurrent.ExecutionContextExecutor
import scala.util.Failure

object Main {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.parseString(
      """
        |akka.http.client.parsing.max-chunk-size=1g
        |akka.http.client.parsing.max-content-length=infinite""".stripMargin)

    implicit val system: ActorSystem = ActorSystem("my-system", ConfigFactory.load(config))
    implicit val materializer: Materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    val file = new File(System.getProperty("user.dir"), "download")
    if (!file.exists()) file.mkdirs()

    val analyzer = new FileAnalyzer(file, _ => false, true)
    val client = new EnderClient("http://localhost:8080", analyzer)
    val time = System.currentTimeMillis()
    client.checkFiles()
      .flatMap(_ => {
        info(s"File checking: ${System.currentTimeMillis() - time}ms")
        client.update(300)
      })
      .onComplete {
        case Failure(exception) => exception.printStackTrace()

        case _ => println(s"All: ${System.currentTimeMillis() - time}ms")
      }
  }
}
