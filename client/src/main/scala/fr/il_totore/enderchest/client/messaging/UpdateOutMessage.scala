package fr.il_totore.enderchest.client.messaging

import java.io.DataOutputStream

import fr.il_totore.enderchest.io.FileChecksum
import fr.il_totore.enderchest.messaging.OutMessage

class UpdateOutMessage(checksums: List[FileChecksum]) extends OutMessage(0) {

  override def write(stream: DataOutputStream): Unit = {
    super.write(stream)
    stream.writeShort(checksums.length)
    for (checksum <- checksums) stream.writeInt(checksum.hash)
  }
}
