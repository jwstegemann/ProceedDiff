package proceed.diff.patch

import proceed.tree.{Element, Node}

/**
  * Created by tiberius on 08.06.16.
  */
sealed trait Patch {
  def apply()
}

case class CreateNewChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def apply() = {
    println("create new Element " + child + " @ " + parent + " before " + sibbling)
  }
}

case class DeleteChild(parent: Element, child: Element) extends Patch {
  def apply() = {
    println("delete Element " + child + " @ " + parent)
  }
}

case class RemoveAttribute(element: Element, attribute: String) extends Patch {
  def apply() = {
    println("  remove Attribute " + attribute + " @ " + element)
  }
}

case class SetAttribute(element: Element, attribute: String, value: String) extends Patch {
  def apply() = {
    println("  set Attribute " + attribute + "=" + value +  " @ " + element)
  }
}

case class MoveChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def apply() = {
    println("move Element " + child + " @ " + parent + " before " + sibbling)
  }
}

