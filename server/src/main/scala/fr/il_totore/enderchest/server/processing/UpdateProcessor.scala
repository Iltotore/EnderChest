package fr.il_totore.enderchest.server.processing

import java.io.DataInputStream

import fr.il_totore.enderchest.io.FileAnalyzer
import fr.il_totore.enderchest.messaging.{InMessage, OutMessage}
import fr.il_totore.enderchest.server.messaging.{DownloadOutMessage, UpdateInMessage}

class UpdateProcessor(analyzer: FileAnalyzer)(implicit registry: List[(Byte, DataInputStream) => InMessage]) extends HttpMessageProcessor {
  override def processMessage(in: InMessage): OutMessage = in match {
    case update: UpdateInMessage =>
      new DownloadOutMessage(analyzer.getChecksum.filter(checksum => !update.getHashes.contains(checksum.hash)))

    case _ => new ErrorMessage("The sent message is not an UpdateMessage")
  }
}