package proceed.tree

import proceed.diff.patch.PatchQueue
import proceed.events.{EventHandler, EventType}
import proceed.tree.html.TextNode
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

  def domNode: DomNode

  override def toString = s"$nodeType($path . $id # ${key.getOrElse()})" // owned by $owner"

  def getNewHandlingComponent(c: Option[Component]) = c

  @tailrec final def handleEvent[A <: EventType](path: Seq[String], handlingComponent: Option[Component], eventType: A)(event: eventType.Event, patchQueue: PatchQueue): Unit = {
    path match {
      case head :: Nil => children.getNode(head) match {
        case Some(handler: EventHandler) => {
          if (handlingComponent.isDefined) handler.handle(handlingComponent.get, eventType)(event, patchQueue)
          else log.error(s"No handling component could be found for event $event at $handler")
        }
        case _ => log.error(s"There is no child with key $head present at $this to handle event $event.")
      }
      case head :: tail => children.getNode(head) match {
        case Some(n: Node) => n.handleEvent(tail, getNewHandlingComponent(handlingComponent), eventType)(event, patchQueue)
        case _ => log.error(s"There is no child with key $head present at $this to delegate event $event to.")
      }
      case x => log.debug(s"Unexpected error handling event:  ${x.toString}")
    }
  }

  final def traverseComponents(toDo: Component => Any): Unit = traverseComponents((0,this) :: Nil, toDo)

  @tailrec
  final private def traverseComponents(nodes: List[(Int,Node)], toDo: Component => Any ): Unit = {
    nodes match {
      case (pos, node: Component) :: tail => {
        toDo(node)
        traverseComponents(node.children.entries ++ tail, toDo)
      }
      case (pos, node: Node) :: tail => {
        traverseComponents(node.children.entries ++ tail, toDo)
      }
      case Nil =>
    }
  }

}

object EmptyNode extends Node {
  key = Some("_")

  override def domNode() = {
    throw new UnsupportedOperationException
  }
}