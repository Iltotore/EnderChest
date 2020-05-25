package fr.il_totore.enderchest.server.handler

import java.io.DataInputStream

import fr.il_totore.enderchest.server.info.{ErrorMessage, InMessage, OutMessage}

class UpdateProcessor(implicit registry: List[(Byte, DataInputStream) => InMessage]) extends HttpMessageProcessor {
  override def processMessage(in: InMessage): OutMessage = new ErrorMessage("TODO")
}