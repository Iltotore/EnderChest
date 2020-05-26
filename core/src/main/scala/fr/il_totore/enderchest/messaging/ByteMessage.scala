package fr.il_totore.enderchest.messaging

import java.io.{DataInputStream, DataOutputStream}

class ByteMessage(id: Byte) {

}

class OutMessage(val id: Byte) extends ByteMessage(id) {

  def write(stream: DataOutputStream): Unit = {
    stream.writeByte(id)
  }
}

class InMessage(val id: Byte) extends ByteMessage(id) {

}

object ByteMessage {

  def apply(stream: DataInputStream)(implicit inMessages: List[(DataInputStream) => InMessage]): InMessage = {
    val id = stream.readByte()
    inMessages(id)(stream)
  }
}