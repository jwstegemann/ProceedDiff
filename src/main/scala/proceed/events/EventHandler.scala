package proceed.events

import proceed.diff.RenderQueue
import proceed.tree.{Component, Element}

trait EventHandler {
  self: Element =>

  private val handlers = new scala.collection.mutable.HashMap[EventType, Any]()

  def handle[T <: EventType, E <: Component](t: T, on: E)(event: t.Event, renderQueue: RenderQueue): Unit = {
    val opt: Option[(E, t.Event) => Unit] = handlers.get(t).asInstanceOf[Option[(E, t.Event) => Unit]]

    opt match {
      case Some(handler) => {
        handler(on, event)
        if (on.dirty) renderQueue.enqueue(on)
      }
      case None => //TODO: output warning
    }
  }

  def on[T <: EventType, E <: Component](t: T, owner: E)(listener: (E, t.Event) => Unit): Element = {
    handlers.put(t, listener)
    this
  }

}
