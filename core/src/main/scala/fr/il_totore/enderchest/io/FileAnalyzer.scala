package fr.il_totore.enderchest.io

import java.io.File

import com.desmondyeung.hashing.XxHash32
import org.apache.commons.io.FileUtils

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}

class FileAnalyzer(directory: File, exclude: String => Boolean, recursive: Boolean) {

  private val checksums = ListBuffer[FileChecksum]()

  def getChecksums: ListBuffer[FileChecksum] = checksums

  def getDirectory: File = directory

  def check(implicit context: ExecutionContextExecutor): Future[Int] = Future {
    checksums.clear()
    checkDirectory(directory, directory)
    checksums.size
  }

  def checkDirectory(root: File, directory: File): Unit = {
    for (file <- directory.listFiles() if !exclude(file.getName)) {
      if (file.isDirectory && recursive) checkDirectory(root, file) else {
        val hash: Int = XxHash32.hashByteArray(FileUtils.readFileToByteArray(file), 0)
        checksums.addOne(FileChecksum(file.getAbsolutePath.substring(root.getAbsolutePath.length + 1), hash))
      }
    }
  }
}
