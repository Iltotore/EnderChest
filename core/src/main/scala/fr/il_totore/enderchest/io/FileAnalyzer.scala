package fr.il_totore.enderchest.io

import java.io.{File, FileInputStream}

import com.desmondyeung.hashing.XxHash32
import org.apache.commons.io.IOUtils

import scala.collection.mutable.ListBuffer

class FileAnalyzer(directory: File, exclude: String => Boolean, recursive: Boolean) {

  private val checksum = ListBuffer[FileChecksum]()

  def getChecksum: ListBuffer[FileChecksum] = checksum

  def check(): Unit = {
    checksum.clear()
    checkDirectory(directory)
  }

  def checkDirectory(directory: File): Unit = {
    for (file <- directory.listFiles() if !exclude.apply(file.getName)) {
      if (file.isDirectory) checkDirectory(file) else {
        val hash: Int = XxHash32.hashByteArray(IOUtils.toByteArray(new FileInputStream(file)), 0)
        checksum.addOne(FileChecksum(file.getAbsolutePath, hash))
      }
    }
  }
}
