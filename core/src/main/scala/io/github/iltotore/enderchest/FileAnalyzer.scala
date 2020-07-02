package io.github.iltotore.enderchest

import java.io.FileInputStream
import java.nio.file.{Files, Path}

import akka.stream.Materializer
import akka.stream.alpakka.file.scaladsl.Directory
import com.desmondyeung.hashing.XxHash32
import org.apache.commons.io.IOUtils

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}

/**
 * Used to generate checksums.
 *
 * @param directory the directory to inspect.
 * @param exclude   the excluding predicate based on the file's relative path.
 */
class FileAnalyzer(directory: Path)(val exclude: String => Boolean = _ => false, maxDepth: Option[Int] = Option.empty, threadCount: Int = Runtime.getRuntime.availableProcessors()) {

  private val checksums = ArrayBuffer[FileChecksum]()

  /**
   * Get generated checksums.
   *
   * @return checksums generated using the check method.
   */
  def getChecksums: ArrayBuffer[FileChecksum] = checksums

  /**
   * Get the root directory.
   *
   * @return the first inspected directory.
   */
  def getDirectory: Path = directory

  /**
   * Check files of the root directory.
   *
   * @param context the context used to generate the future (implicit).
   * @return a Future[Int] containing the generated checksums count.
   */
  def check(implicit context: ExecutionContextExecutor, materializer: Materializer): Future[Int] = {
    checksums.clear()
    if (!Files.exists(directory)) Files.createDirectories(directory)
    Directory.walk(directory, maxDepth)
      .filterNot(path => Files.isDirectory(path))
      .mapAsyncUnordered(threadCount)(path => Future {
        FileChecksum(directory.relativize(path), XxHash32.hashByteArray(IOUtils.toByteArray(new FileInputStream(path.toFile)), 0), path.toFile.length())
      })
      .runFold(checksums)((list, checksum) => {
        list.addOne(checksum)
      }).map(_.size)
  }
}
