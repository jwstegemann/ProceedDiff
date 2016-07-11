package proceed.diff.patch

import proceed.tree.{Element, Node}
import org.scalajs.dom.{document, raw}

/**
  * Created by tiberius on 08.06.16.
  */
sealed trait Patch {
  def apply()
}

//TODO: make it nicer...
case class CreateNewChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def apply() = {
    val gap = "  " * (child.path.count(_ == '.'))
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

    (parent.domRef, sibbling) match {
      case (Some(e: raw.Element), Some(n: Node)) => e.insertBefore(element, n.element.domRef.getOrElse(document.getElementById(n.id)))
      case (None, Some(n: Node)) => document.getElementById(parent.id).insertBefore(element, n.element.domRef.getOrElse(document.getElementById(n.id)))
      case (Some(e: raw.Element), None) => e.appendChild(element)
      case (None, None) => document.getElementById(parent.id).appendChild(element)
      case _ => //TODO produce warning "no parent definded for appending"
    }
    child.domRef = Some(element)
  }
}

//TODO: parent not needed
case class DeleteChild(parent: Element, child: Element) extends Patch {
  def apply() = {
    val gap = "  " * (child.path.count(_ == '.'))
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
  def apply() = {
    val gap = "  " * (element.path.count(_ == '.'))
    println(gap + "  -> remove Attribute " + attribute + " @ " + element)

    element.domRef match {
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

    element.domRef match {
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

    parent.domRef match {
      case Some(e: raw.Element) => document.createElement(child.nodeType)
      case None => document.getElementById(parent.id).appendChild(element)
      case _ => throw new UnsupportedOperationException("no parent definded for appending")
    }
    child.domRef = Some(element)
  }
}

