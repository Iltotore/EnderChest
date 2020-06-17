package io.github.iltotore.enderchest.server

import scala.io.StdIn

class CommandThread(handler: CommandHandler) extends Thread {

  var running = true

  override def run(): Unit = {
    while (running) {
      if (!handler.run(StdIn.readLine())) handler.run("help")
      Thread.sleep(50)
    }
  }
}