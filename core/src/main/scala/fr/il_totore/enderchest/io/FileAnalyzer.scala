package fr.il_totore.enderchest.io

import java.io.File

import com.desmondyeung.hashing.XxHash32
import org.apache.commons.io.FileUtils

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}

/**
 * Used to generate checksums.
 *
 * @param directory the directory to inspect.
 * @param exclude   the excluding predicate based on the file's relative path.
 * @param recursive true to inspect in depth directories.
 */
class FileAnalyzer(directory: File, exclude: String => Boolean, recursive: Boolean) {

  private val checksums = ListBuffer[FileChecksum]()

  /**
   * Get generated checksums.
   *
   * @return checksums generated using the check method.
   */
  def getChecksums: ListBuffer[FileChecksum] = checksums

  /**
   * Get the root directory.
   *
   * @return the first inspected directory.
   */
  def getDirectory: File = directory

  /**
   * Check files of the root directory.
   *
   * @param context the context used to generate the future (implicit).
   * @return a Future[Int] containing the generated checksums count.
   */
  def check(implicit context: ExecutionContextExecutor): Future[Int] = Future {
    checksums.clear()
    checkDirectory(directory, directory)
    checksums.size
  }

  /**
   * Check the given directory.
   *
   * @param root      the root directory used to generate the relative path.
   * @param directory the starting directory.
   */
  def checkDirectory(root: File, directory: File): Unit = {
    for (file <- directory.listFiles()) {

      if (file.isDirectory && recursive) checkDirectory(root, file) else {
        val relativePath = file.getAbsolutePath.substring(root.getAbsolutePath.length + 1)
        if (!exclude(relativePath)) {
          val hash: Int = XxHash32.hashByteArray(FileUtils.readFileToByteArray(file), 0)
          checksums += FileChecksum(relativePath, hash)
        }
      }
    }
  }
}
