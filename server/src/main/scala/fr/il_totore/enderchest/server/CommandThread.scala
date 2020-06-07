package fr.il_totore.enderchest.server

import scala.io.StdIn

class CommandThread(handler: CommandHandler) extends Thread {

  val running = true

  override def run(): Unit = {
    while (running) if (handler.run(StdIn.readLine()))
      handler.run("help")
  }
}
