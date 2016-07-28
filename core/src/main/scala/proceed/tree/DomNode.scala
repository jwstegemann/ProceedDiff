package proceed.tree

import org.scalajs.dom.raw
import proceed.events.EventHandler
import proceed.tree.html.TextNode


trait DomNode extends Node with EventHandler {
  p: Product =>

  type DomNodeRefType <: raw.Node

  val fields: Seq[String]
  var domRef: Option[DomNodeRefType] = None

  def iterator = p.productIterator

  override def as(name: String): DomNode = {
    super.as(name)
    this
  }

}
