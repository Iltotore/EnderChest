package fr.il_totore.enderchest.messaging

import java.io.{DataInputStream, DataOutputStream}

case class ByteMessage(id: Byte) {

}

class OutMessage(override val id: Byte) extends ByteMessage(id) {

  def write(stream: DataOutputStream): Unit = {
    stream.writeByte(id)
  }
}

class InMessage(override val id: Byte) extends ByteMessage(id) {

  def this(id: Byte, stream: DataInputStream) {
    this(id)
  }
}

object ByteMessage {

  def apply(stream: DataInputStream)(implicit inMessages: List[(Byte, DataInputStream) => InMessage]): InMessage = {
    val id = stream.readByte()
    inMessages(id)(id, stream)
  }
}