package io.github.iltotore.enderchest

import java.io.{DataOutputStream, File}
import java.nio.file.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.apache.commons.io.FileUtils
import spray.json.{DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

/**
 * Represent the checksum of a file.
 *
 * @param relativePath the file's relative location.
 * @param hash         the file's hash as int.
 */
case class FileChecksum(relativePath: Path, hash: Int, length: Long) {

  /**
   * Serialize this checksum into an OutputStream.
   *
   * @param directory the root directory to locate file's data.
   * @param stream    the stream to write in.
   */
  def serialize(directory: File, stream: DataOutputStream): Unit = {
    stream.writeUTF(relativePath.toString)
    val bytes = FileUtils.readFileToByteArray(relativePath.toFile)
    stream.writeInt(bytes.length)
    stream.write(bytes)
  }

  override def equals(obj: Any): Boolean = obj match {
    case FileChecksum(relativePath, hash, _) => this.relativePath.equals(relativePath) && this.hash.equals(hash)
    case _ => false
  }
}

object FileChecksum {

  /**
   * Checksum JSON protocol
   */
  object Protocol extends SprayJsonSupport {

    def apply(root: Path): RootJsonFormat[FileChecksum] = new RootJsonFormat[FileChecksum] {
      override def write(obj: FileChecksum): JsValue =
        JsObject(
          "path" -> JsString(obj.relativePath.toString),
          "hash" -> JsNumber(obj.hash)
        )

      override def read(json: JsValue): FileChecksum = json.asJsObject.getFields("path", "hash") match {
        case Seq(JsString(path), JsNumber(hash)) =>
          println(path)
          FileChecksum(root.resolve(path), hash.toIntExact, 0)

        case _ => throw DeserializationException("Invalid checksum")
      }
    }
  }

}