package io.github.iltotore.enderchest.server

import io.github.iltotore.enderchest.server.CommandHandler.Command

import scala.collection.mutable

class CommandHandler {

  val commands: mutable.Map[String, Command] = mutable.HashMap()

  def register(cmd: String, runnable: Command): Unit = commands.put(cmd, runnable)

  def run(cmd: String): Boolean = {
    commands.get(cmd).foreach(_.executor(this))
    commands.contains(cmd)
  }
}

object CommandHandler {

  case class Command(description: String, executor: CommandHandler => Unit)

}