package proceed.tree

import proceed.diff.RenderQueue
import proceed.events.{EventHandler, EventType}

import scala.annotation.tailrec


trait Node {
  var path: String = _
  var id: String = _
  var key: Option[String] = None

  //FIXME: maybe better def
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

  @tailrec final def handleEvent[A <: EventType](path: Seq[String], target: String, eventType: A)(event: eventType.Event, renderQueue: RenderQueue): Unit = {
    path match {
      case head :: Nil => children.getNode(head) match {
        case Some(handler: Component) => handler.handle(eventType, target, handler)(event, renderQueue)
        case _ => //TODO: Error Handling
      }
      case head :: tail => children.getNode(head) match {
        case Some(n: Node) => n.handleEvent(tail, target, eventType)(event, renderQueue)
        case _ => // TODO: Error Handling
      }
    }
  }

}

object EmptyNode extends Node {
  //FIXME: better unique key for empty node
  key = Some("")

  override def element() = {
    throw new UnsupportedOperationException
  }
}

/*
case class TextNode(content: String) extends Node {

}*/
