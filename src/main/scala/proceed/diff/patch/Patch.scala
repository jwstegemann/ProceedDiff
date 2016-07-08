package proceed.diff.patch

import proceed.tree.{Element, Node}
import org.scalajs.dom.{document, raw}

/**
  * Created by tiberius on 08.06.16.
  */
sealed trait Patch {
  def apply()
}

case class CreateNewChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def apply() = {
    val gap = "  " * (child.path.count(_ == '.'))
    println(gap + "create new Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))

    val element = document.createElement(child.nodeType)
    element.id = child.id

    parent.elementDomRef match {
      case Some(e: raw.Element) => e.appendChild(element)
      case None => document.getElementById(parent.id).appendChild(element)
      case _ => throw new UnsupportedOperationException("no parent definded for appending")
    }
    child.elementDomRef = Some(element)
  }
}

//TODO: parent not needed
case class DeleteChild(parent: Element, child: Element) extends Patch {
  def apply() = {
    val gap = "  " * (child.path.count(_ == '.'))
    println(gap + "delete Element " + child + " @ " + parent)

    child.elementDomRef match {
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
  def apply() = {
    val gap = "  " * (element.path.count(_ == '.'))
    println(gap + "  -> remove Attribute " + attribute + " @ " + element)

    element.elementDomRef match {
      case Some(e) => e.removeAttribute(attribute)
      case None => document.getElementById(element.id).removeAttribute(attribute)
      case _ => throw new UnsupportedOperationException("no domRef is defined")
    }
  }
}

case class SetAttribute(element: Element, attribute: String, value: String) extends Patch {
  def apply() = {
    val gap = "  " * (element.path.count(_ == '.'))
    println(gap + "  -> set Attribute " + attribute + "=" + value +  " @ " + element)

    element.elementDomRef match {
      case Some(e) => e.setAttribute(attribute, value)
      case None => document.getElementById(element.id).setAttribute(attribute, value)
      case _ => throw new UnsupportedOperationException("no domRef is defined")
    }
  }
}

case class MoveChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def apply() = {
    val gap = "  " * (child.path.count(_ == '.'))
    println(gap + "move Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))

    val element = document.createElement(child.nodeType)
    element.id = child.id

    parent.elementDomRef match {
      case Some(e: raw.Element) => document.createElement(child.nodeType)
      case None => document.getElementById(parent.id).appendChild(element)
      case _ => throw new UnsupportedOperationException("no parent definded for appending")
    }
    child.elementDomRef = Some(element)
  }
}

