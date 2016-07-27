package proceed.events

import org.scalajs.dom
import org.scalajs.dom.{Event, raw}
import org.scalajs.dom.raw.HTMLInputElement
import proceed.util.log

// http://www.w3schools.com/jsref/dom_obj_event.asp
// https://facebook.github.io/react/docs/events.html
// https://developer.mozilla.org/en-US/docs/Web/Events
sealed trait EventType {
  type Event
  type RawEvent <: dom.Event
  val key: String

  //FIXME: replace by val
  def wrap(rawEvent: RawEvent): Event
}


case class MouseEvent(mouseX: Int, mouseY: Int,
                      altKey: Boolean, ctrlKey: Boolean,
                      metaKey: Boolean, shiftKey: Boolean)

object MouseEvent {
  def fromDomMouseEvent(e: dom.MouseEvent) = MouseEvent(e.clientX.toInt,
    e.clientY.toInt,
    e.altKey,
    e.ctrlKey,
    e.metaKey,
    e.shiftKey)
}



//TODO: target = DomNode[HTMLInputElement] oder sogar proceed.input?
case class TextEvent(input: HTMLInputElement)

object TextEvent {
  def fromDomEvent(e: dom.Event) = TextEvent(e.target.asInstanceOf[HTMLInputElement])

  implicit val inputStringBinder: (TextEvent => String) = _.input.value
  implicit val inputIntBinder: (TextEvent => Int) = _.input.value.toInt
}

case class DragEvent()

object DragEvent {
  def fromDomDragEvent(e: dom.DragEvent) = DragEvent()
}

case class FocusEvent()

object FocusEvent {
  def fromDomFocusEvent(e: dom.FocusEvent) = FocusEvent()
}

case class KeyboardEvent()

object KeyboardEvent {
  def fromDomKeyboardEvent(e: dom.KeyboardEvent) = KeyboardEvent()
}

case class ProceedEvent()

object ProceedEvent {
  def fromDomEvent(e: dom.Event) = ProceedEvent()
}

case class UIEvent()

object UIEvent {
  def fromDomUIEvent(e: dom.UIEvent) = UIEvent()
}

case class WheelEvent()

object WheelEvent {
  def fromDomWheelEvent(e: dom.WheelEvent) = WheelEvent()
}

case class TouchEvent()

object TouchEvent {
  def fromDomTouchEvent(e: dom.TouchEvent) = TouchEvent()
}

case class PopStateEvent()

object PopStateEvent {
  def fromDomPopStateEvent(e: dom.PopStateEvent) = PopStateEvent()
}




/* 
 * Types
 */

object BeforeCopy extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "beforecopy"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object BeforeCut extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "beforecut"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}

object BeforePaste extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "beforepaste"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object Blur extends EventType {
  type Event = FocusEvent
  type RawEvent = dom.FocusEvent
  override val key = "blur"

  override def wrap(rawEvent: RawEvent): Event = FocusEvent.fromDomFocusEvent(rawEvent)
}


object Change extends EventType {
  type Event = ProceedEvent
  type RawEvent = dom.Event
  override val key = "change"

  override def wrap(rawEvent: RawEvent): Event = ProceedEvent.fromDomEvent(rawEvent)
}


object Click extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "click"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object ContextMenu extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "contextmenu"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object Copy extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "copy"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object Cut extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "cut"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object DoubleClick extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "dblclick"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object Drag extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "drag"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object DragEnd extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "dragend"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object DragEnter extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "dragenter"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object DragLeave extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "dragleave"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object DragOver extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "dragover"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object DragStart extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "dragstart"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object Drop extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "drop"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object Focus extends EventType {
  type Event = FocusEvent
  type RawEvent = dom.FocusEvent
  override val key = "focus"

  override def wrap(rawEvent: RawEvent): Event = FocusEvent.fromDomFocusEvent(rawEvent)
}


object FocusIn extends EventType {
  type Event = FocusEvent
  type RawEvent = dom.FocusEvent
  override val key = "focusin"

  override def wrap(rawEvent: RawEvent): Event = FocusEvent.fromDomFocusEvent(rawEvent)
}


object FocusOut extends EventType {
  type Event = FocusEvent
  type RawEvent = dom.FocusEvent
  override val key = "focusout"

  override def wrap(rawEvent: RawEvent): Event = FocusEvent.fromDomFocusEvent(rawEvent)
}


object Input extends EventType {
  type Event = ProceedEvent
  type RawEvent = dom.Event
  override val key = "input"

  override def wrap(rawEvent: RawEvent): Event = ProceedEvent.fromDomEvent(rawEvent)
}


object KeyDown extends EventType {
  type Event = KeyboardEvent
  type RawEvent = dom.KeyboardEvent
  override val key = "keydown"

  override def wrap(rawEvent: RawEvent): Event = KeyboardEvent.fromDomKeyboardEvent(rawEvent)
}


object KeyPress extends EventType {
  type Event = KeyboardEvent
  type RawEvent = dom.KeyboardEvent
  override val key = "keypress"

  override def wrap(rawEvent: RawEvent): Event = KeyboardEvent.fromDomKeyboardEvent(rawEvent)
}


object KeyUp extends EventType {
  type Event = KeyboardEvent
  type RawEvent = dom.KeyboardEvent
  override val key = "keyup"

  override def wrap(rawEvent: RawEvent): Event = KeyboardEvent.fromDomKeyboardEvent(rawEvent)
}


object MouseDown extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "mousedown"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object MouseEnter extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "mouseenter"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object MouseLeave extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "mouseleave"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object MouseMove extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "mousemove"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object MouseOut extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "mouseout"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object MouseOver extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "mouseover"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object MouseUp extends EventType {
  type Event = MouseEvent
  type RawEvent = dom.MouseEvent
  override val key = "mouseup"

  override def wrap(rawEvent: RawEvent): Event = MouseEvent.fromDomMouseEvent(rawEvent)
}


object MouseWheel extends EventType {
  type Event = WheelEvent
  type RawEvent = dom.WheelEvent
  override val key = "mousewheel"

  override def wrap(rawEvent: RawEvent): Event = WheelEvent.fromDomWheelEvent(rawEvent)
}


object Paste extends EventType {
  type Event = DragEvent
  type RawEvent = dom.DragEvent
  override val key = "paste"

  override def wrap(rawEvent: RawEvent): Event = DragEvent.fromDomDragEvent(rawEvent)
}


object PopState extends EventType {
  type Event = PopStateEvent
  type RawEvent = dom.PopStateEvent
  override val key = "popstate"

  override def wrap(rawEvent: RawEvent): Event = PopStateEvent.fromDomPopStateEvent(rawEvent)
}


object Progress extends EventType {
  type Event = ProceedEvent
  type RawEvent = dom.ProgressEvent
  override val key = "progress"

  override def wrap(rawEvent: RawEvent): Event = ProceedEvent.fromDomEvent(rawEvent)
}


object Resize extends EventType {
  type Event = UIEvent
  type RawEvent = dom.UIEvent
  override val key = "resize"

  override def wrap(rawEvent: RawEvent): Event = UIEvent.fromDomUIEvent(rawEvent)
}


object Scroll extends EventType {
  type Event = UIEvent
  type RawEvent = dom.UIEvent
  override val key = "scroll"

  override def wrap(rawEvent: RawEvent): Event = UIEvent.fromDomUIEvent(rawEvent)
}


object Select extends EventType {
  type Event = UIEvent
  type RawEvent = dom.UIEvent
  override val key = "select"

  override def wrap(rawEvent: RawEvent): Event = UIEvent.fromDomUIEvent(rawEvent)
}


object SelectionChange extends EventType {
  type Event = ProceedEvent
  type RawEvent = dom.Event
  override val key = "selectionchange"

  override def wrap(rawEvent: RawEvent): Event = ProceedEvent.fromDomEvent(rawEvent)
}


object SelectStart extends EventType {
  type Event = ProceedEvent
  type RawEvent = dom.Event
  override val key = "selectstart"

  override def wrap(rawEvent: RawEvent): Event = ProceedEvent.fromDomEvent(rawEvent)
}


object Submit extends EventType {
  type Event = ProceedEvent
  type RawEvent = dom.Event
  override val key = "submit"

  override def wrap(rawEvent: RawEvent): Event = ProceedEvent.fromDomEvent(rawEvent)
}


object TouchCancel extends EventType {
  type Event = TouchEvent
  type RawEvent = raw.TouchEvent
  override val key = "touchcancel"

  override def wrap(rawEvent: RawEvent): Event = TouchEvent.fromDomTouchEvent(rawEvent)
}


object TouchEnd extends EventType {
  type Event = TouchEvent
  type RawEvent = raw.TouchEvent
  override val key = "touchend"

  override def wrap(rawEvent: RawEvent): Event = TouchEvent.fromDomTouchEvent(rawEvent)
}


object TouchMove extends EventType {
  type Event = TouchEvent
  type RawEvent = raw.TouchEvent
  override val key = "touchmove"

  override def wrap(rawEvent: RawEvent): Event = TouchEvent.fromDomTouchEvent(rawEvent)
}


object TouchStart extends EventType {
  type Event = TouchEvent
  type RawEvent = raw.TouchEvent
  override val key = "touchstart"

  override def wrap(rawEvent: RawEvent): Event = TouchEvent.fromDomTouchEvent(rawEvent)
}



