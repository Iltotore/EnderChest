package fr.il_totore.enderchest.server.info

import java.io.DataInputStream

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class UpdateInMessage() extends InMessage(1) {

  val hashes: mutable.ListBuffer[Array[Byte]] = ListBuffer()

  def this(id: Byte, stream: DataInputStream) {
    this()
    //TODO hash deserialization
  }
}
