package proceed.events

import org.scalajs.dom.raw

// http://www.w3schools.com/jsref/dom_obj_event.asp
// https://facebook.github.io/react/docs/events.html
// https://developer.mozilla.org/en-US/docs/Web/Events
sealed trait EventType {
  type Event
}

case class MouseEvent(mouseX: Int, mouseY: Int,
                      altKey: Boolean, ctrlKey: Boolean,
                      metaKey: Boolean, shiftKey: Boolean)

case class KeyboardEvent(which: Int, altKey: Boolean,
                         ctrlKey: Boolean, metaKey: Boolean, shiftKey: Boolean)

case class FocusEvent()

case class TargetEvent(domRef: raw.Element)

case class TextEvent(input: raw.HTMLInputElement)

object TextEvent {
  def fromEvent(e: raw.Event) = {
    TextEvent(e.target.asInstanceOf[raw.HTMLInputElement])
  }
}



/*
  Mouse Events
  https://developer.mozilla.org/en-US/docs/Web/API/MouseEvent
 */
object Click extends EventType {
  type Event = MouseEvent
}
object Contextmenu extends EventType {
  type Event = MouseEvent
}
object Dblclick extends EventType {
  type Event = MouseEvent
}
object Mousedown extends EventType {
  type Event = MouseEvent
}
object Mouseenter extends EventType {
  type Event = MouseEvent
}
object Mouseleave extends EventType {
  type Event = MouseEvent
}
object Mousemove extends EventType {
  type Event = MouseEvent
}
object Mouseover extends EventType {
  type Event = MouseEvent
}
object Mouseout extends EventType {
  type Event = MouseEvent
}
object Mouseup extends EventType {
  type Event = MouseEvent
}

/*
  Keyboard Events
  https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent
 */
object Keydown extends EventType {
  type Event = KeyboardEvent
}
object Keypress extends EventType {
  type Event = KeyboardEvent
}
object Keyup extends EventType {
  type Event = KeyboardEvent
}

/*
  Form Events
  https://developer.mozilla.org/en-US/docs/Web/API/FocusEvent
 */
object Blur extends EventType {
  type Event = FocusEvent
}
object Change extends EventType {
  type Event = TargetEvent
}
object Focus extends EventType {
  type Event = FocusEvent
}
object Focusin extends EventType {
  type Event = FocusEvent
}
object Focusout extends EventType {
  type Event = FocusEvent
}
object Input extends EventType {
  type Event = TextEvent
}
object Invalid extends EventType {
  type Event = TargetEvent
}
object Reset extends EventType {
  type Event = TargetEvent
}
object Select extends EventType {
  type Event = TargetEvent
}
object Submit extends EventType {
  type Event = TargetEvent
}
