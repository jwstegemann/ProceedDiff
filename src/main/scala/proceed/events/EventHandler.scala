package proceed.events

import proceed.diff.RenderQueue
import proceed.tree.{Component, Element}

trait EventHandler {
  self: Component =>

  private val handlers = new scala.collection.mutable.HashMap[(EventType, String), Any]()

  def handle[T <: EventType, E <: Component](t: T, target: String, on: E)(event: t.Event, renderQueue: RenderQueue): Unit = {
    val opt: Option[(E, t.Event) => Unit] = handlers.get(t, target).asInstanceOf[Option[(E, t.Event) => Unit]]

    opt match {
      case Some(handler) => {
        handler(on, event)
        if (this.dirty) renderQueue.enqueue(this)
      }
      case None => //TODO: output warning
    }
  }

  def register[T <: EventType](t: T)(target: String, listener: Any): Unit = {
    handlers.put((t, target), listener)
  }

}

trait EventListener {
  self: Element =>

  def on[T <: EventType, E <: Component](t: T, owner: E)(listener: (E, t.Event) => Unit): Element = {
//    owner.register(t)(s":${path.substring(owner.path.length + owner.id.length+2)}.$id", listener)
    owner.register(t)(":0.dummy", listener)
    this
  }

}
