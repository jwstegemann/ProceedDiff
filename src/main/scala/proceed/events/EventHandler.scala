package proceed.events

import proceed.diff.patch.PatchQueue
import proceed.diff.{RenderItem, RenderQueue}
import proceed.tree.{Component, Element, StatefullComponent}
import proceed.util.log

trait EventHandler {
  self: Element =>

  private val handlers = new scala.collection.mutable.HashMap[EventType, Any]()

  def handle[T <: EventType, E <: Component](t: T, on: E)(event: t.Event, renderQueue: RenderQueue, patchQueue: PatchQueue): Unit = {

    val opt: Option[(E, t.Event) => Unit] = handlers.get(t).asInstanceOf[Option[(E, t.Event) => Unit]]

    opt match {
      case Some(handler) => {
        on match {
          case sc: StatefullComponent[Product] => {
            val oldState = sc.state
            handler(on, event)
            if (sc.dirty && sc.shouldRender(oldState)) {
              renderQueue.enqueue(RenderItem(sc, sc.parent, None, patchQueue))
            }
          }
          case c => {
            handler(on, event)
          }
        }
      }
      case None => log.warn(s"No event handler for event $event was found at ${this.toString}")
    }
  }

  def on[T <: EventType, E <: Component](t: T, owner: E)(listener: (E, t.Event) => Unit): Element = {
    handlers.put(t, listener)
    this
  }

}
