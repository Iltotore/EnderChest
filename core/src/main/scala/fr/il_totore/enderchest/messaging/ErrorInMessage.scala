package fr.il_totore.enderchest.messaging

import java.io.DataInputStream

class ErrorInMessage(message: String) extends InMessage(0) {

  def this(stream: DataInputStream) {
    this(stream.readUTF())
  }
}
