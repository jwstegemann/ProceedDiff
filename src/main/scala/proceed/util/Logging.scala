package proceed.util

import org.scalajs.dom.console


object log {

  val DEBUG=16
  val INFO=8
  val WARN = 4
  val ERROR = 2
  val FATAL = 1

  var threshold = INFO

  def setThreshold(level: Int) = {
    threshold = level

    debug = if (threshold >= DEBUG) outputMessage else doNothing
    info = if (threshold >= INFO) outputInfo else doNothing
    warn = if (threshold >= WARN) outputWarn else doNothing
    error = if (threshold >= ERROR) outputError else doNothing
  }

  private def doNothing(msg: String) = {}
  private def outputMessage(msg: String) = {println(msg)}
  private def outputInfo(msg: String) = {console.info(msg)}
  private def outputWarn(msg: String) = {console.warn(msg)}
  private def outputError(msg: String) = {console.error(msg)}

  var debug = doNothing _
  var info = outputInfo _
  var warn = outputWarn _
  var error = outputError _
  var fatal = outputError _
}
