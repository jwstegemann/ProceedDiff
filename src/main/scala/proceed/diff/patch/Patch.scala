package proceed.diff.patch

import proceed.tree.{Element, Node}
import proceed.util.log

import scala.collection.mutable

/**
  * Created by tiberius on 08.06.16.
  */
sealed trait Patch {
  def execute()
}

case class CreateNewChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def execute() = {
    //TODO: handle TextNode with document.createTextNode here
    val gap = "  " * (child.path.count(_ == '.'))
    log.debug(gap + "create new Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))
  }
}

case class DeleteChild(parent: Element, child: Element) extends Patch {
  def execute() = {
    val gap = "  " * (child.path.count(_ == '.'))
    log.debug(gap + "delete Element " + child + " @ " + parent)
  }
}

case class RemoveAttribute(element: Element, attribute: String) extends Patch {
  def execute() = {
    val gap = "  " * (element.path.count(_ == '.'))
    log.debug(gap + "  -> remove Attribute " + attribute + " @ " + element)
  }
}

case class SetAttribute(element: Element, attribute: String, value: String) extends Patch {
  def execute() = {
    val gap = "  " * (element.path.count(_ == '.'))
    log.debug(gap + "  -> set Attribute " + attribute + "=" + value +  " @ " + element)
  }
}

case class MoveChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def execute() = {
    val gap = "  " * (child.path.count(_ == '.'))
    log.debug(gap + "move Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))
  }
}

class PatchQueue extends mutable.Queue[Patch] with Patch {

  def execute() = foreach(_.execute)

}

