package proceed.diff.patch

import java.net.HttpCookie

import org.scalajs.dom
import org.scalajs.dom.raw
import proceed.tree.html.TextNode
import proceed.tree.{Element, Node}
import proceed.util.log

/**
  * Created by tiberius on 08.06.16.
  */
sealed trait Patch {
  def execute()

  def doWithParentDomRef(parent: Element, toDo: raw.Node => Any) = {
    parent.domRef match {
      case Some(parentDomRef) => toDo(parentDomRef)
      case None => log.error(s"Parent $parent has no domRef yet.")
    }
  }

  def doWithSibblingDomRef(sibbling: Option[Node], toDo: raw.Node => Any, elseDo: () => Any) = {
    sibbling.flatMap(_.element.domRef) match {
      case Some(sibblingDomRef) => toDo(sibblingDomRef)
      case None => elseDo()
    }
  }

}

case class CreateNewChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def execute() = {
    //TODO: handle TextNode with document.createTextNode here
    val gap = "  " * (child.path.count(_ == '.'))
    log.debug(gap + "create new Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))

    child match {
      case textNode: TextNode => textNode.domRef = Some(dom.document.createTextNode(textNode.content))
      case element: Element => {
        val newDomElement = dom.document.createElement(child.nodeType)
        newDomElement.setAttribute("id",child.childrensPath)
        element.domRef = Some(newDomElement)
      }
    }

    doWithParentDomRef(parent, parentDomRef =>
      doWithSibblingDomRef(sibbling,
        sibblingDomRef => parentDomRef.insertBefore(child.domRef.get, sibblingDomRef),
        () => parentDomRef.appendChild(child.domRef.get)
      )
    )
  }
}

case class DeleteChild(parent: Element, child: Element) extends Patch {
  def execute() = {
    val gap = "  " * (child.path.count(_ == '.'))
    log.debug(gap + "delete Element " + child + " @ " + parent)
    println(gap + "delete Element " + child + " @ " + parent)

/*    child.domRef match {
      case Some(domRef) => e.parentNode.removeChild(e)
      case None => {
        val e = document.getElementById(child.id)
        e.parentNode.removeChild(e)
      }
      case _ => throw new UnsupportedOperationException("no parent definded for appending")
    }*/
  }
}

case class RemoveAttribute(element: Element, attribute: String) extends Patch {
  def execute() = {
    val gap = "  " * (element.path.count(_ == '.'))
    log.debug(gap + "  -> remove Attribute " + attribute + " @ " + element)
    println(gap + "  -> remove Attribute " + attribute + " @ " + element)

 /*   element.domRef match {
      case Some(e) => e.removeAttribute(attribute)
      case None => document.getElementById(element.id).removeAttribute(attribute)
      case _ => throw new UnsupportedOperationException("no domRef is defined")
    }*/
  }
}

case class SetAttribute(element: Element, attribute: String, value: String) extends Patch {
  def execute() = {
    val gap = "  " * (element.path.count(_ == '.'))
    log.debug(gap + "  -> set Attribute " + attribute + "=" + value +  " @ " + element)
    println(gap + "  -> set Attribute " + attribute + "=" + value +  " @ " + element)

    element.domRef match {
        //FIXME: kein Cast!!!
      case Some(e) => e.asInstanceOf[raw.Element].setAttribute(attribute, value)
      case None => log.error(s"Element $element has no domRef yet.")
    }
  }
}

case class MoveChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def execute() = {
    val gap = "  " * (child.path.count(_ == '.'))
    log.debug(gap + "move Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))
    println(gap + "move Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))

/*    val element = document.createElement(child.nodeType)
    element.id = child.id

    parent.domRef match {
      case Some(e: raw.Element) => document.createElement(child.nodeType)
      case None => document.getElementById(parent.id).appendChild(element)
      case _ => throw new UnsupportedOperationException("no parent definded for appending")
    }
    child.domRef = Some(element)*/
  }
}

class PatchQueue extends scala.collection.mutable.Queue[Patch] with Patch {

  def execute() = foreach(_.execute)

}

