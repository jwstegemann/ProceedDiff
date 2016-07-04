package proceed.tree


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

  override def toString = s"$nodeType($path . $id # $key)"
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

object MountPoint {
  def apply(id: String) = {
    val mp = new MountPoint()
    mp.id = id
    mp
  }
}

case class MountPoint() extends Element {
  override val fields: Seq[String] = Nil

  override def apply(c: Node, cs: Node*): Element = throw new UnsupportedOperationException
  override def apply(cs: Seq[Node]): Element with Product = throw new UnsupportedOperationException
  override def apply(): Element = throw new UnsupportedOperationException



}

/*
case class TextNode(content: String) extends Node {

}*/
