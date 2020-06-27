package io.github.iltotore.enderchest.client

import java.io.{File, FileOutputStream, OutputStream}

import akka.Done
import akka.util.ByteString
import io.github.iltotore.enderchest.EndLogger._

/**
 * An object used to flush and download received bytes.
 *
 * @param root the directory to download in.
 */
class ChunkedDownloader(root: File, onProgress: (Long, Long) => Unit) {

  var count: Long = -1
  var downloaded = 0
  var deleting = false
  var stream: OutputStream = _

  /**
   * Process the given chunked ByteString.
   *
   * @param byteString the ByteString to process.
   * @return this ChunkedDownloader for chaining.
   */
  def apply(byteString: ByteString): ChunkedDownloader = {
    if (count < 0) {
      println("Get size")
      count = byteString.utf8String.toLong
      println(count)
    }
    else if (deleting) {
      println("Deleting " + byteString.utf8String)
      new File(root, byteString.utf8String).delete()
    } else if (byteString.utf8String.equals("ENDERCHEST_FILE_REMOVE")) {
      info("Starting to delete files...")
      deleting = true
    } else if (byteString.utf8String.equals("ENDERCHEST_FILE_SEPARATOR")) {
      if (stream == null) return this
      stream.close()
      stream = null
    } else if (stream == null) {

      val file = new File(root, byteString.utf8String)
      if (!file.getParentFile.exists()) file.getParentFile.mkdirs()
      if (!file.exists()) file.createNewFile()
      stream = new FileOutputStream(file)
    } else {
      downloaded += byteString.length
      onProgress(downloaded, count)
      stream.write(byteString.toArray)
    }
    this
  }

  /**
   * Close the last stream.
   * @return Done.
   */
  def close(): Done = {
    if (stream != null) stream.close()
    Done
  }
}