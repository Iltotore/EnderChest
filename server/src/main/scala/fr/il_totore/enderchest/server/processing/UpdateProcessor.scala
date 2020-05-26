package fr.il_totore.enderchest.server.processing

import java.io.DataInputStream

import fr.il_totore.enderchest.messaging.{InMessage, OutMessage}
import fr.il_totore.enderchest.processing.HttpMessageProcessor
import fr.il_totore.enderchest.server.messaging.{ErrorMessage, UpdateInMessage}

class UpdateProcessor(implicit registry: List[(Byte, DataInputStream) => InMessage]) extends HttpMessageProcessor {
  override def processMessage(in: InMessage): OutMessage = in match {
    case update: UpdateInMessage => {
      update.getHashes
      new ErrorMessage("TODO")
    }

    case _ => new ErrorMessage("The sent message is not an UpdateMessage")
  }
}