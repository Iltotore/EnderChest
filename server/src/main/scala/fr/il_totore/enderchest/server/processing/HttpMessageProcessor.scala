package fr.il_totore.enderchest.server.processing

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, DataOutputStream}

import fr.il_totore.enderchest.messaging.{ByteMessage, InMessage, OutMessage}

abstract class HttpMessageProcessor(implicit registry: List[(Byte, DataInputStream) => InMessage]) extends HttpByteStreamProcessor {
  override def processStream(input: ByteArrayInputStream): ByteArrayOutputStream = {
    val out = processMessage(ByteMessage(new DataInputStream(input)))
    val byteStream = new ByteArrayOutputStream
    val dataStream = new DataOutputStream(byteStream)
    out.write(dataStream)
    byteStream
  }

  def processMessage(in: InMessage): OutMessage
}