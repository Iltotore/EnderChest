package io.github.iltotore.enderchest.server

import java.io.File
import java.util.logging.Level

import akka.actor.ActorSystem
import akka.stream.Materializer
import io.github.iltotore.enderchest.EndLogger._

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("enderchest")
    implicit val materializer: Materializer = Materializer(system)
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val directory = new File(System.getProperty("user.dir"))

    val app: Server = new Server(new File(directory, "config.yml"))
    info("Starting server...")
    app.start()
    info("Indexing files...")
    val time = System.currentTimeMillis()
    app.analyzer.check.onComplete {
      case Success(count) => fine(s"Successfully indexed $count files in ${System.currentTimeMillis() - time}ms")

      case Failure(exception) => log(Level.WARNING, "Unable to process files.", exception)
    }

    val cmdHandler = new CommandHandler
    val cmdThread = new CommandThread(cmdHandler)
    cmdHandler.register("help", DefaultCommands.help)
    cmdHandler.register("stop", DefaultCommands.stop(app, cmdThread))
    cmdHandler.register("top", DefaultCommands.top)
    cmdThread.start()
  }
}
