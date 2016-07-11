package proceed.tree

import org.scalajs.dom.raw
import proceed.events.{EventHandler}


abstract class Element extends Node with EventHandler {
  p: Product =>

  val fields: Seq[String]
  var domRef: Option[raw.Node] = None


  override def as(name: String): Element = {
    super.as(name)
    this
  }

  def apply(c: Node, cs: Node*): Element = {
    apply(c +: cs)
    this
  }

  def apply(cs: Seq[Node]): Element = {
    children = new ChildMapImpl
    cs.zipWithIndex.foreach {
      case (n: Node, i: Int) => children.add(i, n)
    }
    this
  }

  def apply(): Element = {
    this
  }

  override def element() = this

  def iterator = p.productIterator
}
