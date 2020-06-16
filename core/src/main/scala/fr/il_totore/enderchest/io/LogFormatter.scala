package fr.il_totore.enderchest.io

import java.text.SimpleDateFormat
import java.util.logging.{Formatter, LogRecord}

/**
 * The EnderChest's log formatter.
 */
class LogFormatter extends Formatter {

  private val dateFormat = new SimpleDateFormat("hh:mm:ss")

  override def format(record: LogRecord): String =
    s"[${dateFormat.format(record.getMillis)}][${record.getLevel.getName}] ${record.getMessage}\n"
}
