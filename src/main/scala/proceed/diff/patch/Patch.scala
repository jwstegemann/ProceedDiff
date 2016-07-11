package proceed.diff.patch

import proceed.tree.{Element, Node}

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
    println(gap + "create new Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))

    val element = document.createElement(child.nodeType)
    element.id = child.id

    for ((name: String, (value: Option[Any])) <- child.fields.iterator.zip(child.iterator)) {
      value match {
        case None =>
        case Some(s: String) => {
          if(name.equals("className")) element.setAttribute("class", s)
          else element.setAttribute(name, s)
        }
        case Some(i: Int) => element.setAttribute(name, i.toString)
        case Some(b: Boolean) => element.setAttribute(name, b.toString)
        case _ => //TODO produce warning
      }
    }

case class DeleteChild(parent: Element, child: Element) extends Patch {
  def execute() = {
    val gap = "  " * (child.path.count(_ == '.'))
    log.debug(gap + "delete Element " + child + " @ " + parent)
    println(gap + "delete Element " + child + " @ " + parent)

    child.domRef match {
      case Some(e: raw.Element) => e.parentNode.removeChild(e)
      case None => {
        val e = document.getElementById(child.id)
        e.parentNode.removeChild(e)
      }
      case _ => throw new UnsupportedOperationException("no parent definded for appending")
    }
  }
}

case class RemoveAttribute(element: Element, attribute: String) extends Patch {
  def execute() = {
    val gap = "  " * (element.path.count(_ == '.'))
    log.debug(gap + "  -> remove Attribute " + attribute + " @ " + element)
    println(gap + "  -> remove Attribute " + attribute + " @ " + element)

    element.domRef match {
      case Some(e) => e.removeAttribute(attribute)
      case None => document.getElementById(element.id).removeAttribute(attribute)
      case _ => throw new UnsupportedOperationException("no domRef is defined")
    }
  }
}

case class SetAttribute(element: Element, attribute: String, value: String) extends Patch {
  def execute() = {
    val gap = "  " * (element.path.count(_ == '.'))
    log.debug(gap + "  -> set Attribute " + attribute + "=" + value +  " @ " + element)
    println(gap + "  -> set Attribute " + attribute + "=" + value +  " @ " + element)

    element.domRef match {
      case Some(e) => e.setAttribute(attribute, value)
      case None => document.getElementById(element.id).setAttribute(attribute, value)
      case _ => throw new UnsupportedOperationException("no domRef is defined")
    }
  }
}

case class MoveChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def execute() = {
    val gap = "  " * (child.path.count(_ == '.'))
    log.debug(gap + "move Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))
    println(gap + "move Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))

    val element = document.createElement(child.nodeType)
    element.id = child.id

    parent.domRef match {
      case Some(e: raw.Element) => document.createElement(child.nodeType)
      case None => document.getElementById(parent.id).appendChild(element)
      case _ => throw new UnsupportedOperationException("no parent definded for appending")
    }
    child.domRef = Some(element)
  }
}

class PatchQueue extends mutable.Queue[Patch] with Patch {

  def execute() = foreach(_.execute)

}

