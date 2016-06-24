package proceed.tree


trait Node {
  var path: String = _
  var name: Option[String] = None

  val typeName: String

  def as(name: String) = {
    this.name = Some(name)
    this
  }

  /*
  def @#(name: String) = {
    as(name)
  }
  */

}

object EmptyNode extends Node {
  name = None
  val typeName = "_EmptyNode"
}

abstract class Element extends Node {
  p: Product =>

  //TODO: make optional
  var children = new ChildMap

  def apply(c: Node, cs: Node*): Element = {
    apply(c +: cs)
    this
  }

  def apply(cs: Seq[Node]) = {

    cs.zipWithIndex.foreach {
      case (n: Node, i: Int) => children.add(i, n)
    }

    this
  }

  def apply(): Element = {
    this
  }

}
