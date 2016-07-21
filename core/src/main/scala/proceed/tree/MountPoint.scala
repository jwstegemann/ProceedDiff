package proceed.tree

import java.lang.reflect.MalformedParametersException

import org.scalajs.dom
import proceed.App
import proceed.diff.RenderQueue
import proceed.diff.patch.PatchQueue
import proceed.events._
import proceed.util.log

object MountPoint {
  def apply(id: String) = new MountPoint().init(id)
}

case class MountPoint() extends Element {
  override val fields: Seq[String] = Nil

  override def apply(c: Node, cs: Node*): Element = throw new UnsupportedOperationException
  override def apply(cs: Seq[Node]): Element with Product = throw new UnsupportedOperationException
  override def apply(): Element = throw new UnsupportedOperationException

  def init(domId: String): MountPoint = {
    id = domId
    val temp = dom.document.getElementById(id)
    if (temp == null) throw new RuntimeException(s"There is no element with id $domId to serve as MountPoint")
    domRef = Some(Left(temp))
    setEventListener()
    this
  }

  def setEventListener() = {
    domRef match {
      case (Some(Left(domElementRef))) => {
        domElementRef.addEventListener("input", handleNativeEvent("input") _, false)
        domElementRef.addEventListener("click", handleNativeEvent("click") _, false)
        domElementRef.addEventListener("keypress", handleNativeEvent("keypress") _, false)
        domElementRef.addEventListener("change", handleNativeEvent("change") _, false)
        domElementRef.addEventListener("input", handleNativeEvent("input") _, false)
      }
      case _ => log.debug(s"MountPoint $this is not available to register event-listeners.")
    }
  }

  def handleNativeEvent[T <: dom.Event](rawTypeString: String)(rawEvent: T) = {
    rawEvent.stopPropagation()
    //rawEvent.preventDefault()

    val path = rawEvent.srcElement.getAttribute("id").split('.').tail.toList

    log.debug(s"handling $rawTypeString-event ${rawEvent} of ${rawEvent.`type`} @ $path")

    App.startEventLoop((patchQueue: PatchQueue) =>
      //FIXME: change order
      (rawEvent, rawEvent.`type`) match {
        case (e: dom.MouseEvent, "click") =>
          handleEvent(path, None, Click)(
            MouseEvent(e.clientX.toInt, e.clientY.toInt, e.altKey, e.ctrlKey, e.metaKey, e.shiftKey), patchQueue)
        case (e: dom.KeyboardEvent, "keypress") =>
          handleEvent(path, None, Keypress)(
            KeyboardEvent(e.keyCode, e.altKey, e.ctrlKey, e.metaKey, e.shiftKey), patchQueue)
        case (e: dom.Event, "change") =>
          handleEvent(path, None, Change)(
            TargetEvent(e.srcElement), patchQueue)
        case (e: dom.Event, "input") =>
          handleEvent(path, None, Input)(
            TextEvent.fromEvent(e), patchQueue)
      }
    )
    
  }
}

