package fr.il_totore.enderchest.server.messaging

import java.io.DataInputStream

import fr.il_totore.enderchest.messaging.InMessage

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class UpdateInMessage() extends InMessage(1) {

  private val hashes: mutable.ListBuffer[Array[Byte]] = ListBuffer()

  def this(id: Byte, stream: DataInputStream) {
    this()
    val hashCount = stream.readUnsignedShort()
    for (_ <- 0 to hashCount) {
      var hashLength = stream.readUnsignedByte()
      hashLength = stream.readUnsignedByte()
      val hash = new Array[Byte](hashLength)
      stream.readFully(hash)
      hashes.addOne(hash)
    }
  }

  def getHashes: mutable.ListBuffer[Array[Byte]] = hashes
}