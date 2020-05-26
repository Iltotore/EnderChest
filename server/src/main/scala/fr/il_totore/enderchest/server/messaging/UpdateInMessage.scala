package fr.il_totore.enderchest.server.messaging

import java.io.DataInputStream

import fr.il_totore.enderchest.messaging.InMessage

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class UpdateInMessage() extends InMessage(1) {

  private val hashes: mutable.ListBuffer[Int] = ListBuffer()

  def this(stream: DataInputStream) {
    this()
    val hashCount = stream.readUnsignedShort()
    for (_ <- 0 to hashCount) hashes.addOne(stream.readInt())
  }

  def getHashes: mutable.ListBuffer[Int] = hashes
}