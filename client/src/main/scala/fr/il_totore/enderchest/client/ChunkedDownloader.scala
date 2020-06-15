package fr.il_totore.enderchest.client

import java.io.{File, FileOutputStream, OutputStream}

import akka.Done
import akka.util.ByteString
import fr.il_totore.enderchest.io.EndLogger._

class ChunkedDownloader(root: File) {

  var deleting = false
  var stream: OutputStream = _

  def process(byteString: ByteString): ChunkedDownloader = {
    if (deleting) {
      info("Deleting " + byteString.utf8String)
      new File(root, byteString.utf8String).delete()
    } else if (byteString.utf8String.equals("ENDERCHEST_FILE_REMOVE")) {
      info("Starting to delete files...")
      deleting = true
    } else if (byteString.utf8String.equals("ENDERCHEST_FILE_SEPARATOR")) {
      if (stream == null) return this
      stream.close()
      stream = null
    } else if (stream == null) {
      info(s"Beginning download of '${byteString.utf8String}'")

      val file = new File(root, byteString.utf8String)
      if (!file.getParentFile.exists()) file.getParentFile.mkdirs()
      if (!file.exists()) file.createNewFile()
      stream = new FileOutputStream(file)
    } else {
      stream.write(byteString.toArray)
    }
    this
  }

  def close(): Done = {
    if (stream != null) stream.close()
    Done
  }
}