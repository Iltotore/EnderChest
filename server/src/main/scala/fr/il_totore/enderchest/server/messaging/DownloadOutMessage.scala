package fr.il_totore.enderchest.server.messaging

import java.io.{DataOutputStream, FileInputStream}
import java.util.zip.GZIPInputStream

import fr.il_totore.enderchest.io.FileChecksum
import fr.il_totore.enderchest.messaging.OutMessage
import org.apache.commons.io.IOUtils

import scala.collection.mutable

class DownloadOutMessage(checksums: mutable.ListBuffer[FileChecksum]) extends OutMessage(1) {

  override def write(stream: DataOutputStream): Unit = {
    super.write(stream)
    stream.writeShort(checksums.length)
    for (checksum <- checksums) {
      val relativePath = checksum.file.getAbsolutePath.substring(checksum.directory.getAbsolutePath.length)
      val fileLength = checksum.file.length()
      val fileStream = new GZIPInputStream(new FileInputStream(checksum.file))
      stream.writeUTF(relativePath)
      stream.writeLong(fileLength)
      IOUtils.copy(fileStream, stream)
    }
  }
}
