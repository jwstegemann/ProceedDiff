package proceed.events

import proceed.tree.Element

trait EventHandler {
  self: Element =>

  private val handlers = new scala.collection.mutable.HashMap[EventType, Any]()

  def on[T <: EventType](t: T)(listener: (t.Event => Unit)): Element = {
    handlers.put(t, listener)
    this
  }

  def handle[T <: EventType](t: T)(event: t.Event): Unit = {
    val opt: Option[(t.Event => Unit)] = handlers.get(t).asInstanceOf[Option[(t.Event => Unit)]]

    opt match {
      case Some(handler) => handler(event)
      case None =>
    }
  }

}
