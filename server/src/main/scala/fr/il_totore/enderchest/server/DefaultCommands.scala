package fr.il_totore.enderchest.server

import akka.actor.ActorSystem
import fr.il_totore.enderchest.io.EndLogger._
import fr.il_totore.enderchest.server.CommandHandler.Command

object DefaultCommands {

  def help: Command = Command("Show help", handler => {
    val help = new StringBuilder(System.lineSeparator()).append("==== EnderChest Help ====").append(System.lineSeparator())
    for (cmd <- handler.commands)
      help.append(cmd._1).append(": ").append(cmd._2.description).append(System.lineSeparator())
    info(help.toString())
  })

  def stop(actor: ActorSystem, commandThread: CommandThread): Command = Command("Stop the server", _ => {
    info("Stopping server...")
    actor.terminate()
    commandThread.running = false
  })

}