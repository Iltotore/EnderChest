package fr.il_totore.enderchest.io

import java.io.{File, FileInputStream}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.apache.commons.io.IOUtils
import spray.json.{DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}


case class FileChecksum(relativePath: String, hash: Int) {

  def toUpdateInfo(directory: File): UpdateInfo = {
    val bytes = IOUtils.toByteArray(new FileInputStream(new File(directory, relativePath)))
    println("bytes " + bytes.length)
    UpdateInfo(relativePath, bytes)
  }
}

object FileChecksum {


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

/*class ChecksumStreamingSupport private[il_totore](
                                              maxObjectSize: Int,
                                              val supported: ContentTypeRange,
                                              val contentType: ContentType,
                                              val framingRenderer: Flow[ByteString, ByteString, NotUsed],
                                              val parallelism: Int,
                                              val unordered: Boolean
                                            ) extends EntityStreamingSupport {
  def this(maxObjectSize: Int) =
    this(
      maxObjectSize,
      ContentTypeRange(ContentTypes.`application/octet-stream`),
      ContentTypes.`application/octet-stream`,
      Flow[ByteString].intersperse(ByteString("["), ByteString(","), ByteString("]")),
      1, false)

  override def framingDecoder: Flow[ByteString, ByteString, NotUsed] = ???

  override def withSupported(range: jm.ContentTypeRange): EntityStreamingSupport = new ChecksumStreamingSupport(maxObjectSize, range, contentType, framingRenderer, parallelism, unordered)

  override def withContentType(contentType: jm.ContentType): EntityStreamingSupport = new ChecksumStreamingSupport(maxObjectSize, supported, contentType, framingRenderer, parallelism, unordered)

  override def withParallelMarshalling(parallelism: Int, unordered: Boolean): EntityStreamingSupport = ???
}*/