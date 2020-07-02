package io.github.iltotore.enderchest

import java.util.ResourceBundle
import java.util.function.Supplier
import java.util.logging._

/**
 * The EnderChest's logger.
 */
object EndLogger {

  private val stream = EndLogger.getClass.getResourceAsStream("/logging.properties")
  LogManager.getLogManager.readConfiguration(stream)

  private val LOGGER = Logger.getAnonymousLogger
  LOGGER.setLevel(Level.FINER)

  def getResourceBundle: ResourceBundle = LOGGER.getResourceBundle

  def setResourceBundle(bundle: ResourceBundle): Unit = LOGGER.setResourceBundle(bundle)

  def getResourceBundleName: String = LOGGER.getResourceBundleName

  def getFilter: Filter = LOGGER.getFilter

  def setFilter(newFilter: Filter): Unit = LOGGER.setFilter(newFilter)

  def log(record: LogRecord): Unit = LOGGER.log(record)

  def log(level: Level, msg: String): Unit = LOGGER.log(level, msg)

  def log(level: Level, msgSupplier: Supplier[String]): Unit = LOGGER.log(level, msgSupplier)

  def log(level: Level, msg: String, param1: Any): Unit = LOGGER.log(level, msg, param1)

  def log(level: Level, msg: String, params: Array[AnyRef]): Unit = LOGGER.log(level, msg, params)

  def log(level: Level, msg: String, thrown: Throwable): Unit = LOGGER.log(level, msg, thrown)

  def log(level: Level, thrown: Throwable, msgSupplier: Supplier[String]): Unit = LOGGER.log(level, thrown, msgSupplier)

  def logp(level: Level, sourceClass: String, sourceMethod: String, msg: String): Unit = LOGGER.logp(level, sourceClass, sourceMethod, msg)

  def logp(level: Level, sourceClass: String, sourceMethod: String, msgSupplier: Supplier[String]): Unit = LOGGER.logp(level, sourceClass, sourceMethod, msgSupplier)

  def logp(level: Level, sourceClass: String, sourceMethod: String, msg: String, param1: Any): Unit = LOGGER.logp(level, sourceClass, sourceMethod, msg, param1)

  def logp(level: Level, sourceClass: String, sourceMethod: String, msg: String, params: Array[AnyRef]): Unit = LOGGER.logp(level, sourceClass, sourceMethod, msg, params)

  def logp(level: Level, sourceClass: String, sourceMethod: String, msg: String, thrown: Throwable): Unit = LOGGER.logp(level, sourceClass, sourceMethod, msg, thrown)

  def logp(level: Level, sourceClass: String, sourceMethod: String, thrown: Throwable, msgSupplier: Supplier[String]): Unit = LOGGER.logp(level, sourceClass, sourceMethod, thrown, msgSupplier)

  def logrb(level: Level, sourceClass: String, sourceMethod: String, bundleName: String, msg: String): Unit = LOGGER.logrb(level, sourceClass, sourceMethod, bundleName, msg)

  def logrb(level: Level, sourceClass: String, sourceMethod: String, bundleName: String, msg: String, param1: Any): Unit = LOGGER.logrb(level, sourceClass, sourceMethod, bundleName, msg, param1)

  def logrb(level: Level, sourceClass: String, sourceMethod: String, bundleName: String, msg: String, params: Array[AnyRef]): Unit = LOGGER.logrb(level, sourceClass, sourceMethod, bundleName, msg, params)

  def logrb(level: Level, sourceClass: String, sourceMethod: String, bundle: ResourceBundle, msg: String, params: Any*): Unit = LOGGER.logrb(level, sourceClass, sourceMethod, bundle, msg, params)

  def logrb(level: Level, sourceClass: String, sourceMethod: String, bundleName: String, msg: String, thrown: Throwable): Unit = LOGGER.logrb(level, sourceClass, sourceMethod, bundleName, msg, thrown)

  def logrb(level: Level, sourceClass: String, sourceMethod: String, bundle: ResourceBundle, msg: String, thrown: Throwable): Unit = LOGGER.logrb(level, sourceClass, sourceMethod, bundle, msg, thrown)

  def entering(sourceClass: String, sourceMethod: String): Unit = LOGGER.entering(sourceClass, sourceMethod)

  def entering(sourceClass: String, sourceMethod: String, param1: Any): Unit = LOGGER.entering(sourceClass, sourceMethod, param1)

  def entering(sourceClass: String, sourceMethod: String, params: Array[AnyRef]): Unit = LOGGER.entering(sourceClass, sourceMethod, params)

  def exiting(sourceClass: String, sourceMethod: String): Unit = LOGGER.exiting(sourceClass, sourceMethod)

  def exiting(sourceClass: String, sourceMethod: String, result: Any): Unit = LOGGER.exiting(sourceClass, sourceMethod, result)

  def throwing(sourceClass: String, sourceMethod: String, thrown: Throwable): Unit = LOGGER.throwing(sourceClass, sourceMethod, thrown)

  def severe(msg: String): Unit = LOGGER.severe(msg)

  def warning(msg: String): Unit = LOGGER.warning(msg)

  def info(msg: String): Unit = LOGGER.info(msg)

  def config(msg: String): Unit = LOGGER.config(msg)

  def fine(msg: String): Unit = LOGGER.fine(msg)

  def finer(msg: String): Unit = LOGGER.finer(msg)

  def finest(msg: String): Unit = LOGGER.finest(msg)

  def severe(msgSupplier: Supplier[String]): Unit = LOGGER.severe(msgSupplier)

  def warning(msgSupplier: Supplier[String]): Unit = LOGGER.warning(msgSupplier)

  def info(msgSupplier: Supplier[String]): Unit = LOGGER.info(msgSupplier)

  def config(msgSupplier: Supplier[String]): Unit = LOGGER.config(msgSupplier)

  def fine(msgSupplier: Supplier[String]): Unit = LOGGER.fine(msgSupplier)

  def finer(msgSupplier: Supplier[String]): Unit = LOGGER.finer(msgSupplier)

  def finest(msgSupplier: Supplier[String]): Unit = LOGGER.finest(msgSupplier)

  def getLevel: Level = LOGGER.getLevel

  def setLevel(newLevel: Level): Unit = LOGGER.setLevel(newLevel)

  def isLoggable(level: Level): Boolean = LOGGER.isLoggable(level)

  def getName: String = LOGGER.getName

  def addHandler(handler: Handler): Unit = LOGGER.addHandler(handler)

  def removeHandler(handler: Handler): Unit = LOGGER.removeHandler(handler)

  def getHandlers: Array[Handler] = LOGGER.getHandlers

  def getUseParentHandlers: Boolean = LOGGER.getUseParentHandlers

  def setUseParentHandlers(useParentHandlers: Boolean): Unit = LOGGER.setUseParentHandlers(useParentHandlers)

  def getParent: Logger = LOGGER.getParent

  def setParent(parent: Logger): Unit = LOGGER.setParent(parent)

}
