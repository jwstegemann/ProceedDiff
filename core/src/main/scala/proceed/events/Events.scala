package proceed.events

import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLInputElement
import proceed.util.log

// http://www.w3schools.com/jsref/dom_obj_event.asp
// https://facebook.github.io/react/docs/events.html
// https://developer.mozilla.org/en-US/docs/Web/Events
sealed trait EventType {
  type Event
  type RawEvent <: dom.Event
  val key: String

  def wrap(rawEvent: RawEvent): Event
}


case class MouseEvent(mouseX: Int, mouseY: Int,
                      altKey: Boolean, ctrlKey: Boolean,
                      metaKey: Boolean, shiftKey: Boolean)

/* case class KeyboardEvent(which: Int, altKey: Boolean,
                         ctrlKey: Boolean, metaKey: Boolean, shiftKey: Boolean)

case class FocusEvent()
*/


//TODO: target = DomNode[HTMLInputElement] oder sogar proceed.input?
case class TextEvent(input: HTMLInputElement)

object TextEvent {
  implicit val inputStringBinder: (TextEvent => String) = _.input.value
  implicit val inputIntBinder: (TextEvent => Int) = _.input.value.toInt
}



/*
  Mouse Events
  https://developer.mozilla.org/en-US/docs/Web/API/MouseEvent
 */
object Click extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "click"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent(
    rawEvent.clientX.toInt,
    rawEvent.clientY.toInt,
    rawEvent.altKey,
    rawEvent.ctrlKey,
    rawEvent.metaKey,
    rawEvent.shiftKey
  )
}

object Input extends EventType {
  type Event = TextEvent
  type RawEvent = dom.Event
  override val key = "input"

  override def wrap(rawEvent: RawEvent): Event = TextEvent(rawEvent.target.asInstanceOf[HTMLInputElement])
}
