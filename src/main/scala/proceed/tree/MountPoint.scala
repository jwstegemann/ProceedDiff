package proceed.tree

import org.scalajs.dom
import proceed.diff.patch.PatchQueue
import proceed.events._

object MountPoint {
  def apply(id: String) = {
    val mp = new MountPoint()
    mp.id = id
    mp.setEventListener()
    mp
  }
}

case class MountPoint() extends Element {
  override val fields: Seq[String] = Nil

  override def apply(c: Node, cs: Node*): Element = throw new UnsupportedOperationException
  override def apply(cs: Seq[Node]): Element with Product = throw new UnsupportedOperationException
  override def apply(): Element = throw new UnsupportedOperationException

  def setEventListener() = {
    //TODO: implement set event-listener
  }

  /*
 * event-handling
 */

  //TODO: take DOM-Eevent and get/create params from there
  def handleEvent[A <: EventType](targetId: String, eventType: A)(event: eventType.Event): Unit = {
    val target = targetId.split("\\.").toList //FIXME: better way to split string?
    if (target.head == id) {
      children.getFirstChild().handleEvent(target.tail, eventType)(event)
      // Use RenderQueue, etc.
      val patchQueue = new PatchQueue
      children.getFirstChild().asInstanceOf[Component].render(patchQueue, this, None)
      patchQueue.execute()
    }
  }

  //FIXME: put in extra-class, join with handleEvent from above
  def handleNativeEvent(rawEvent: dom.Event) = {
    rawEvent.stopPropagation()
    rawEvent.preventDefault()

    val targetId = rawEvent.srcElement.getAttribute("id")

    //debug(s"handling ${rawEvent.`type`} event for $targetId")

    //FIXME: change order
    (rawEvent, rawEvent.`type`) match {
      //TODO: create proceed.MouseEvent from dom.MouseEvent
      case (e: dom.MouseEvent, "click") =>
        handleEvent(targetId, Click)(
          MouseEvent(e.clientX.toInt, e.clientY.toInt, e.altKey, e.ctrlKey, e.metaKey, e.shiftKey))
      case (e: dom.KeyboardEvent, "keypress") =>
        handleEvent(targetId, Keypress)(
          KeyboardEvent(e.keyCode, e.altKey, e.ctrlKey, e.metaKey, e.shiftKey))
      case (e: dom.Event, "change") =>
        handleEvent(targetId, Change)(
          TargetEvent(e.srcElement))
      case (e: dom.Event, "input") =>
        handleEvent(targetId, Input)(
          TargetEvent(e.srcElement))
    }
  }
}

