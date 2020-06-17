package io.github.iltotore.enderchest

import java.io.{DataOutputStream, File}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.apache.commons.io.FileUtils
import spray.json.{DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

/**
 * Represent the checksum of a file.
 *
 * @param relativePath the file's relative location.
 * @param hash         the file's hash as int.
 */
case class FileChecksum(relativePath: String, hash: Int) {

  /**
   * Serialize this checksum into an OutputStream.
   *
   * @param directory the root directory to locate file's data.
   * @param stream    the stream to write in.
   */
  def serialize(directory: File, stream: DataOutputStream): Unit = {
    stream.writeUTF(relativePath)
    val bytes = FileUtils.readFileToByteArray(new File(directory, relativePath))
    stream.writeInt(bytes.length)
    stream.write(bytes)
  }
}

object FileChecksum {

  /**
   * Checksum JSON protocol
   */
  object Protocol extends SprayJsonSupport {

    implicit val format: RootJsonFormat[FileChecksum] = new RootJsonFormat[FileChecksum] {
      override def write(obj: FileChecksum): JsValue =
        JsObject(
          "path" -> JsString(obj.relativePath),
          "hash" -> JsNumber(obj.hash)
        )

      override def read(json: JsValue): FileChecksum = json.asJsObject.getFields("path", "hash") match {
        case Seq(JsString(path), JsNumber(hash)) => FileChecksum(path, hash.toIntExact)

        case _ => throw DeserializationException("Invalid checksum")
      }
    }
  }

}