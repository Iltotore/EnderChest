package io.github.iltotore.enderchest.server

import java.lang.management.ManagementFactory

import io.github.iltotore.enderchest.EndLogger._
import io.github.iltotore.enderchest.server.CommandHandler.Command

import scala.concurrent.Await
import scala.concurrent.duration._

object DefaultCommands {

  def help: Command = Command("Show help", handler => {
    val help = new StringBuilder(System.lineSeparator()).append("==== EnderChest Help ====").append(System.lineSeparator())
    for (cmd <- handler.commands)
      help.append(cmd._1).append(": ").append(cmd._2.description).append(System.lineSeparator())
    info(help.toString())
  })

  def stop(server: Server): Command = Command("Stop the server", _ => {
    info("Stopping server...")
    val time = System.currentTimeMillis()
    Await.ready(server.stop(), 15.seconds)
    if (System.currentTimeMillis() - time >= 15000) info("Stopping process was too long! Skipping...")
    info("Bye !")
  })

  def top: Command = Command("Show memory statistics", _ => {
    info("==== EnderChest Analysis ====")
    info(s"Memory Usage: ${(Runtime.getRuntime.totalMemory() - Runtime.getRuntime.freeMemory()) / 1024D} KB")
    info(s"Heap: ${ManagementFactory.getMemoryMXBean.getHeapMemoryUsage}")
  })
}