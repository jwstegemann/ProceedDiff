package proceed.tree.html

import proceed.tree.{Element, Node}

case class TextNode(content: String) extends Element {
  override val fields: Seq[String] = "content" :: Nil

  override def apply(c: Node, cs: Node*): Element = throw new UnsupportedOperationException
  override def apply(cs: Seq[Node]): Element = throw new UnsupportedOperationException
  override def apply(): Element = throw new UnsupportedOperationException
}
