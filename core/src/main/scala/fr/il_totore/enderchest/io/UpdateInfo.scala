package fr.il_totore.enderchest.io

import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPOutputStream

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, DeserializationException, JsObject, JsString, JsValue, RootJsonFormat}


case class UpdateInfo(path: String, data: Array[Byte])

object UpdateInfo {

  object Protocol extends SprayJsonSupport with DefaultJsonProtocol {

    implicit val format: RootJsonFormat[UpdateInfo] = new RootJsonFormat[UpdateInfo] {
      override def write(obj: UpdateInfo): JsValue = {
        val byteStream = new ByteArrayOutputStream()
        val gzipStream = new GZIPOutputStream(byteStream)
        gzipStream.write(obj.data)
        gzipStream.close()
        byteStream.close()
        JsObject(
          "path" -> JsString(obj.path),
          "data" -> JsString(Base64.getEncoder.encodeToString(byteStream.toByteArray))
        )
      }

      override def read(json: JsValue): UpdateInfo = json.asJsObject.getFields("path", "hash") match {
        case Seq(JsString(path), JsString(data)) => UpdateInfo(path, data.getBytes)

        case _ => throw DeserializationException("Invalid checksum")
      }
    }
  }

}