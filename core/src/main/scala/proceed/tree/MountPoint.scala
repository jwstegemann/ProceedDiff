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
        domElementRef.addEventListener(Input.key, handleNativeEvent(Input) _, false)
        domElementRef.addEventListener(Click.key, handleNativeEvent(Click) _, false)
//        domElementRef.addEventListener("keypress", handleNativeEvent("keypress") _, false)
//        domElementRef.addEventListener("change", handleNativeEvent("change") _, false)
//        domElementRef.addEventListener("input", handleNativeEvent("input") _, false)
      }
      case _ => log.debug(s"MountPoint $this is not available to register event-listeners.")
    }
  }

  def handleNativeEvent(eventType: EventType)(rawEvent: eventType.RawEvent) = {
    rawEvent.stopPropagation()
    //rawEvent.preventDefault()

    val path = rawEvent.srcElement.getAttribute("data-proceed").split('.').tail.toList

    log.debug(s"handling ${eventType.key}-event ${rawEvent} of ${rawEvent.`type`} @ $path")

    App.startEventLoop((patchQueue: PatchQueue) =>
      handleEvent(path, None, eventType)(rawEvent, patchQueue)
    )
    
  }
}

