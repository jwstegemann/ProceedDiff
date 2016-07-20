package proceed.diff.patch

import org.scalajs.dom
import org.scalajs.dom.raw
import org.scalajs.dom.raw.Text
import proceed.tree.html.TextNode
import proceed.tree.{Element, Node}
import proceed.util.log


sealed trait Patch {
  def execute()

  def doWithDomElementRef(parent: Element, toDo: raw.Element => Any) = {
    parent.domRef match {
      case Some(Left(parentDomRef)) => toDo(parentDomRef)
      case Some(Right(_)) => {}
      case None => log.error(s"Parent $parent has no domRef yet.")
    }
  }

  def doWithDomTextRef(parent: Element, toDo: raw.Text => Any): Any = {
    parent.domRef match {
      case Some(Right(parentDomRef)) => toDo(parentDomRef)
      case Some(Left(_)) => {}
      case None => log.error(s"Parent $parent has no domRef yet.")
    }
  }

  def doWithDomNodeRef(parent: Element, toDo: raw.Node => Any): Any = {
    parent.domRef match {
      case Some(Right(parentDomRef)) => toDo(parentDomRef)
      case Some(Left(parentDomRef)) => toDo(parentDomRef)
      case None => log.error(s"Parent $parent has no domRef yet.")
    }
  }

  def doWithSibblingDomRef(sibbling: Option[Node], toDo: raw.Node => Any, elseDo: () => Any): Any = {
    sibbling.flatMap(_.element.domRef) match {
      case Some(Right(sibblingDomRef)) => toDo(sibblingDomRef)
      case Some(Left(sibblingDomRef)) => toDo(sibblingDomRef)
      case None => elseDo()
    }
  }

  def gap(child: Element) = "  " * (child.path.count(_ == '.'))

}

case class CreateNewChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def execute() = {
    log.debug(gap(child) + "create new Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))

    child match {
      case textNode: TextNode => textNode.domRef = Some(Right(dom.document.createTextNode(textNode.content)))
      case element: Element => {
        val newDomElement = dom.document.createElement(child.nodeType)
        newDomElement.setAttribute("id",child.childrensPath)
        element.domRef = Some(Left(newDomElement))
      }
    }

    doWithDomElementRef(parent, parentDomRef =>
      doWithSibblingDomRef(sibbling,
        sibblingDomRef => doWithDomNodeRef(child, parentDomRef.insertBefore(_,sibblingDomRef)),
        () => doWithDomNodeRef(child, parentDomRef.appendChild(_))
      )
    )
  }
}

case class DeleteChild(parent: Element, child: Element) extends Patch {
  def execute() = {
    log.debug(gap(child) + "delete Element " + child + " @ " + parent)

    doWithDomElementRef(parent, parentDomRef =>
      doWithDomNodeRef(child, parentDomRef.removeChild(_))
    )
  }
}

case class RemoveAttribute(element: Element, attribute: String) extends Patch {
  def execute() = {
    log.debug(gap(element) + "  -> remove Attribute " + attribute + " @ " + element)

    doWithDomElementRef(element, _.removeAttribute(attribute))
  }
}

case class SetAttribute(element: Element, attribute: String, value: String) extends Patch {
  def execute() = {
    log.debug(gap(element) + "  -> set Attribute " + attribute + "=" + value +  " @ " + element)

    doWithDomElementRef(element, _.setAttribute(attribute, value))
  }
}

case class ChangeText(element: TextNode, parent: Element, value: String) extends Patch {
  override def execute() = {
    log.debug(gap(element) + "  -> changing Text to " + value +  " @ " + element)}

    doWithDomTextRef(element, textRef => textRef.replaceData(0,textRef.length,value))
}

case class MoveChild(parent: Element, child: Element, sibbling: Option[Node]) extends Patch {
  def execute() = {
    log.debug(gap(child) + "move Element " + child + " @ " + parent + " before " + sibbling.map(s => s.element))

    doWithDomElementRef(parent, parentDomRef =>
      doWithSibblingDomRef(sibbling,
        sibblingDomRef => doWithDomNodeRef(child, parentDomRef.insertBefore(_,sibblingDomRef)),
        () => doWithDomNodeRef(child, parentDomRef.appendChild(_))
      )
    )
  }
}

class PatchQueue extends scala.collection.mutable.Queue[Patch] with Patch {

  def execute() = foreach(_.execute)

}

