package io.github.iltotore.enderchest

import java.nio.file.Path

object Implicits {

  implicit class PathUtil(path: Path) {

    def isSimilarTo(other: Path): Boolean = path.toString.replace("\\", "/") equals
      other.toString.replace("\\", "/")
  }

}
