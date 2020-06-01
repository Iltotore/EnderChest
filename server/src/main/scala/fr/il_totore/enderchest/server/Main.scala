package fr.il_totore.enderchest.server

import java.io.File
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.stream.{ActorMaterializer, Materializer}
import spray.json._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.FiniteDuration
import scala.io.StdIn

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: Materializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()
      .withParallelMarshalling(6, unordered = true)

    val directory = new File(System.getProperty("user.dir"))

    val app: Server = new Server(args, new File(directory, "config.yml"))
    app.start()
    app.analyzer.check

    Http().bindAndHandleAsync(request => {
      request.entity.toStrict(FiniteDuration(1, TimeUnit.MINUTES)).map(strict => app.receiveUpdatePart(strict.data.utf8String.parseJson.asInstanceOf[JsArray]))
    }, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return

  }
}
