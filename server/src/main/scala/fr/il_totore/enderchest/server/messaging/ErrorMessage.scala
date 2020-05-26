package fr.il_totore.enderchest.server.messaging

import java.io.DataOutputStream

import fr.il_totore.enderchest.messaging.OutMessage

class ErrorMessage(message: String) extends OutMessage(0) {
  override def write(stream: DataOutputStream): Unit = {
    super.write(stream)
    stream.writeChars(message)
  }
}