package fr.il_totore.enderchest.io

import java.util.logging.{ConsoleHandler, Level}

class LogHandler extends ConsoleHandler {

  setLevel(Level.FINER)
  setEncoding("UTF-8")
  setOutputStream(System.out)
  setFormatter(new LogFormatter)
}
