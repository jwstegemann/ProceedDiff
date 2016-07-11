package proceed.tree

import proceed.diff.RenderQueue
import proceed.diff.patch.PatchQueue
import proceed.events.{EventHandler, EventType}
import proceed.util.log

import scala.annotation.tailrec


trait Node {
  var path: String = _
  var id: String = _
  var key: Option[String] = None

  lazy val childrensPath = s"$path.$id"

  //TODO: write macro for this (and maybe even generate hash)
  lazy val nodeType: String = this.getClass.getSimpleName()

  def as(name: String) = {
    this.key = Some(name)
    this
  }

  var children: ChildMap = NoChildsMap

  /*
  def @#(name: String) = {
    as(name)
  }
  */

  def element: Element

  override def toString = s"$nodeType($path . $id # ${key.getOrElse()})" // owned by $owner"

  def getNewHandlingComponent(c: Option[Component]) = c

  @tailrec final def handleEvent[A <: EventType](path: Seq[String], handlingComponent: Option[Component], eventType: A)(event: eventType.Event, renderQueue: RenderQueue, patchQueue: PatchQueue): Unit = {
    path match {
      case head :: Nil => children.getNode(head) match {
        case Some(handler: EventHandler) => {
          if (!handlingComponent.isEmpty) handler.handle(eventType, handlingComponent.get)(event, renderQueue, patchQueue)
          else log.error(s"No handling component could be found for event $event at $handler")
        }
        case _ => log.error(s"There is no child with key $head present at $this to handle event $event.")
      }
      case head :: tail => children.getNode(head) match {
        case Some(n: Node) => n.handleEvent(tail, getNewHandlingComponent(handlingComponent), eventType)(event, renderQueue, patchQueue)
        case _ => log.error(s"There is no child with key $head present at $this to delegate event $event to.")
      }
      case x => log.debug(x.toString())
    }
  }

}

object EmptyNode extends Node {
  key = Some("_")

  override def element() = {
    throw new UnsupportedOperationException
  }
}

