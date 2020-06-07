package fr.il_totore.enderchest.server

import fr.il_totore.enderchest.server.CommandHandler.Command

import scala.collection.mutable

class CommandHandler {

  val commands: mutable.Map[String, Command] = mutable.HashMap()

  def register(cmd: String, runnable: Command): Unit = commands.put(cmd, runnable)

  def run(cmd: String): Boolean = {
    commands.get(cmd).foreach(_.executor)
    commands.contains(cmd)
  }
}

object CommandHandler {

  case class Command(description: String, executor: CommandHandler => Unit)

}