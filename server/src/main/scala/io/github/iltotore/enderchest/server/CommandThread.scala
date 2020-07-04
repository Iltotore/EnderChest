package io.github.iltotore.enderchest.server

class CommandThread(handler: CommandHandler) extends Thread {

  var running = true

  override def run(): Unit = {
    while (running) {
      Thread.sleep(50)
    }
  }
}