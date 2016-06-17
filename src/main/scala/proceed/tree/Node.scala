package proceed.tree

import scala.collection.mutable


trait Node {
  var path: String = _
  var name: Option[String] = None

  def as(name: String) = {
    this.name = Some(name)
    this
  }

}

abstract class Element extends Node {
  p: Product =>

  //TODO: make optional
  var children: Option[mutable.LinkedHashMap[String, Node]] = None

  def apply(c: Node, cs: Node*): Element = {
    apply(c +: cs)
    this
  }

  def apply(cs: Seq[Node]) = {
    children = Some(new mutable.LinkedHashMap[String, Node]())

    cs.zipWithIndex.foreach {
      case (n: Node, i: Int) => children.get.put(n.name.getOrElse(i.toString), n)
    }

    this
  }

  def apply(): Element = {
    this
  }

}
