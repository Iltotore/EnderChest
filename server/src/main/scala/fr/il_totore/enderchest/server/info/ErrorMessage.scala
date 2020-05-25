package fr.il_totore.enderchest.server.info

import java.io.DataOutputStream

class ErrorMessage(message: String) extends OutMessage(0) {
  override def write(stream: DataOutputStream): Unit = {
    super.write(stream)
    stream.writeChars(message)
  }
}