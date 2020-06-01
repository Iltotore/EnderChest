package fr.il_totore.enderchest.server

import java.io.File
import java.nio.file.Files
import java.util.regex.Pattern

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import fr.il_totore.enderchest.io.UpdateInfo.Protocol._
import fr.il_totore.enderchest.io.{FileAnalyzer, FileChecksum}
import org.simpleyaml.configuration.file.YamlConfiguration
import spray.json.{JsNumber, JsObject, JsString, _}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}

class Server(args: Array[String], configFile: File)(implicit system: ActorSystem, materializer: Materializer, contextExecutor: ExecutionContextExecutor) {

  var bindingFuture: Future[ServerBinding] = _
  var analyzer: FileAnalyzer = _

  def start(): Unit = {
    if (!configFile.exists()) Files.copy(getClass.getResourceAsStream("/config.yml"), configFile.toPath)
    val config = YamlConfiguration.loadConfiguration(configFile)
    val dir = new File(config.getString("file.directory"))
    if (!dir.exists()) dir.mkdirs()
    val pattern = Pattern.compile(config.getString("file.exclude")).asPredicate()
    val exclude = pattern.test _
    analyzer = new FileAnalyzer(dir, exclude, config.getBoolean("file.recursive"))
  }

  def checkFiles: Future[Done] = analyzer.check

  def stop(): Unit = {
    bindingFuture.flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def receiveUpdatePart(array: JsArray): HttpResponse = {
    val receivedChecksums = new ArrayBuffer[FileChecksum]()
    for (value <- array.elements if value.isInstanceOf[JsObject]) {
      processChecksum(value.asJsObject).foreach(receivedChecksums.addOne)
    }

    println(analyzer.getChecksums.size)


    val requiredFiles = for (value <- analyzer.getChecksums if !receivedChecksums.contains(value)) yield
      HttpEntity.Chunk(value.toUpdateInfo(analyzer.getDirectory).toJson.compactPrint)

    println(s"Sending ${requiredFiles.size} files")
    HttpResponse(entity = HttpEntity.Chunked(ContentTypes.`application/json`, Source(requiredFiles.toVector)))
  }

  def processChecksum(json: JsObject): Option[FileChecksum] = {
    json.getFields("path", "hash") match {
      case Seq(JsString(path), JsNumber(hash)) => Option(new FileChecksum(path, hash.toIntExact))

      case _ => Option.empty
    }
  }
}
