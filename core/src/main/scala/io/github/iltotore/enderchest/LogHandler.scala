package io.github.iltotore.enderchest

import java.util.logging.{ConsoleHandler, Level}

/**
 * The EnderChest log handler.
 */
class LogHandler extends ConsoleHandler {

  setLevel(Level.ALL)
  setEncoding("UTF-8")
  setOutputStream(System.out)
  setFormatter(new LogFormatter)
}