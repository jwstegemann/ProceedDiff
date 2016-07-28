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

  implicit def string2Node(s: String): TextNode = TextNode(s)
  implicit def string2Option(s: String): Option[String] = Some(s)
  implicit def boolean2Option(b: Boolean): Option[Boolean] = Some(b)
  implicit def int2Option(i: Int): Option[Int] = Some(i)

  implicit class String2ClassName(s: String) {
    def add(x: String): ClassName = ClassName(s) add ClassName(x)
    def addif(x: ClassName)(f: => Boolean) = ClassName(s).addif(x)(f)
  }
}
