package proceed.events

import proceed.App
import proceed.diff.patch.PatchQueue
import proceed.diff.{RenderItem, RenderQueue}
import proceed.tree.{Component, Element, StatefullComponent}
import proceed.util.log

trait EventHandler {
  self: Element =>

  private val handlers = new scala.collection.mutable.HashMap[EventType, Any]()

  def handle[T <: EventType, E <: Component](t: T, on: E)(event: t.Event, patchQueue: PatchQueue): Unit = {

    log.debug(s"handling event $event on $on")

    val opt: Option[(E, t.Event) => Unit] = handlers.get(t).asInstanceOf[Option[(E, t.Event) => Unit]]

    log.debug(s"opt=$opt")

    opt match {
      case Some(handler) => {
        on match {
          case sc: StatefullComponent[Product] => {
            log.debug(s"statefull")
            val oldState = sc.state
            handler(on, event)
            if (sc.dirty && sc.shouldRender(oldState)) {
              App.renderQueue.enqueue(RenderItem(sc.durable, sc.parent, None, patchQueue))
            }
          }
          case c => {
            log.debug(s"stateless $c")
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
