package io.github.iltotore.enderchest.client

import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import io.github.iltotore.enderchest.FileAnalyzer

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
    val path = Paths.get(System.getProperty("user.dir"), "download")
    if (!Files.exists(path)) Files.createFile(path)

    implicit val progressStatus: (Long, Long) => Unit =
      (downloaded, max) => println(s"Progress: ${downloaded.doubleValue / max * 100}% ($downloaded/$max)")
    implicit val downloadLogger: FileDownloadAction = file => println(s"Downloading ${file.getName}...")
    implicit val deleteLogger: FileDeleteAction = file => println(s"Deleting ${file.getName}...")

    val analyzer = new FileAnalyzer(path, exclude = _ => true)
    val client = new EnderClient("http://localhost:8080", analyzer)
    val time = System.currentTimeMillis()
    client.checkFiles
      .flatMap(_ => client.update)
      .onComplete {
        case Failure(exception) => exception.printStackTrace()

        case _ => println(s"All: ${System.currentTimeMillis() - time}ms")
      }
  }
}
