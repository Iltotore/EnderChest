package fr.il_totore.enderchest.messaging

import java.io.DataOutputStream

class ErrorOutMessage(message: String) extends OutMessage(0) {
  override def write(stream: DataOutputStream): Unit = {
    super.write(stream)
    stream.writeUTF(message)
  }
}