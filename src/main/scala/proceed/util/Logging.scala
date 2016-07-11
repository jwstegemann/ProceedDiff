package proceed.util


object log {

  val DEBUG=16
  val INFO=8
  val WARN = 4
  val ERROR = 2
  val FATAL = 1

  var threshold = DEBUG

  def setThreshold(level: Int) = {
    threshold = level

    debug = if (threshold >= DEBUG) outputMessage _ else doNothing _
    info = if (threshold >= INFO) outputMessage _ else doNothing _
    warn = if (threshold >= WARN) outputMessage _ else doNothing _
    error = if (threshold >= ERROR) outputMessage _ else doNothing _
  }

  private def doNothing(msg: String) = {}
  private def outputMessage(msg: String) = {println(msg)}

  var debug = outputMessage _
  var info = outputMessage _
  var warn = outputMessage _
  var error = outputMessage _
  var fatal = outputMessage _
}
