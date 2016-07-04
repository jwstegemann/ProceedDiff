package proceed.tree


trait Node {
  var path: String = _
  var id: String = _
  var key: Option[String] = None

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

}

object EmptyNode extends Node {
  //FIXME: better unique key for empty node
  key = Some("")

  override def element() = {
    throw new UnsupportedOperationException
  }
}

abstract class Element extends Node {
  p: Product =>

  val fields: Seq[String]
  var elementDomRef: String = "unknown"

  def apply(c: Node, cs: Node*): Element = {
    apply(c +: cs)
    this
  }

  def apply(cs: Seq[Node]) = {
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
/*
case class TextNode(content: String) extends Node {

}*/
