package proceed.events

import proceed.App
import proceed.diff.patch.PatchQueue
import proceed.diff.{RenderItem}
import proceed.tree.{Component, Element, StatefullComponent}
import proceed.util.log
import scala.collection.mutable
import proceed.events.Input

trait EventHandler {
  self: Element =>

  type Handler = (EventType, Any)

  val handlers = new mutable.HashMap[EventType, Any]


  def handle[X <: EventType, C <: Component](on: C, t: X)(event: t.Event, patchQueue: PatchQueue): Unit = {
    handlers.get(t).foreach {
      case f => {
        val handler = f.asInstanceOf[(C) => (t.Event) => Any]
        on match {
          case sc: StatefullComponent[Product] => {
            val oldState = sc.state
            log.debug(s"calling handler for ${event.toString} on $on")
            handler(on)(event)
            if (sc.dirty && sc.shouldRender(oldState)) {
              App.renderQueue.enqueue(RenderItem(sc.durable, sc.parent, None, patchQueue))
            }
          }
          case c => {
            handler(on)(event)
          }
        }
      }
    }
  }

  def !(entry: Handler): Element = {
    handlers += entry
    self
  }

  def apply(handlerList: Handler*) = {
    handlerList.foreach(handlers += _)
    self
  }

}


trait EventDelegate {
  c: Component =>

  final def on[T <: EventType](t: T)(handler: this.type => (t.Event => Any)) = (t, handler)

  final def onInput = on(Input) _

}
