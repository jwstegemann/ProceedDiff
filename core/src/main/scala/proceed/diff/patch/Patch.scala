package proceed.diff.patch

import org.scalajs.dom
import org.scalajs.dom.raw
import proceed.tree.html.TextNode
import proceed.tree.{ClassName, Element, Node}
import proceed.util.log


sealed trait Patch {
  def execute()

  def doWithDomElementRef(parent: Element, toDo: raw.Element => Any) = {
    parent.domRef match {
      case Some(parentDomRef: raw.Element) => toDo(parentDomRef)
      case Some(_) =>
      case None => log.error(s"Parent $parent has no domRef yet.")
    }
  }

  def doWithDomTextRef(parent: TextNode, toDo: raw.Text => Any): Any = {
    parent.domRef match {
      case Some(parentDomRef: raw.Text) => toDo(parentDomRef)
      case Some(_) =>
      case None => log.error(s"Parent $parent has no domRef yet.")
    }
  }

  def doWithDomNodeRef(parent: Node, toDo: raw.Node => Any): Any = {
    parent match {
      case e: Element => toDo(e.domRef.get)
      case t: TextNode =>  toDo(t.domRef.get)
      case _ => log.error(s"Parent $parent is not an Element or TextNode.")
    }
  }

  def doWithSibblingDomRef(sibbling: Option[Node], toDo: raw.Node => Any, elseDo: () => Any): Any = {
    sibbling match {
      case Some(e: Element) => toDo(e.domRef.get)
      case Some(t: TextNode) => toDo(t.domRef.get)
      case _ => elseDo()
    }
  }

  def gap(child: Node) = "  " * child.path.count(_ == '.')

}

case class CreateNewChild(parent: Element, child: Node, sibbling: Option[Node]) extends Patch {
  def execute() = {
    log.debug(gap(child) + "create new Element " + child + " @ " + parent + " before " + sibbling)

    child match {
      case textNode: TextNode => textNode.domRef = Some(dom.document.createTextNode(textNode.content))
      case element: Element => {
        val newDomElement = dom.document.createElement(element.nodeType)
        if(child.key.isDefined) newDomElement.id = child.key.get
        newDomElement.setAttribute("data-proceed",element.childrensPath)
        element.domRef = Some(newDomElement)
      }
    }

    doWithDomElementRef(parent, parentDomRef =>
      doWithSibblingDomRef(sibbling,
        sibblingDomRef => doWithDomNodeRef(child, parentDomRef.insertBefore(_,sibblingDomRef)),
        () => doWithDomNodeRef(child, parentDomRef.appendChild)
      )
    )
  }
}

case class DeleteChild(parent: Element, child: Node) extends Patch {
  def execute() = {
    log.debug(gap(child) + "delete Element " + child + " @ " + parent)

    doWithDomElementRef(parent, parentDomRef =>
      doWithDomNodeRef(child, parentDomRef.removeChild)
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

case class SetClassName(element: Element, className: ClassName) extends Patch {
  def execute() = {
    log.debug(gap(element) + "  -> set ClassNames " + className +  " @ " + element)
    if(className.size > 0)
      doWithDomElementRef(element, _.setAttribute("class", className.toString()))
  }
}

case class ChangeText(element: TextNode, parent: Element, value: String) extends Patch {
  override def execute() = {
    log.debug(gap(element) + "  -> changing Text to " + value +  " @ " + element)}

    doWithDomTextRef(element, textRef => textRef.replaceData(0,textRef.length,value))
}

case class MoveChild(parent: Element, child: Node, sibbling: Option[Node]) extends Patch {
  def execute() = {
    log.debug(gap(child) + "move Element " + child + " @ " + parent + " before " + sibbling)

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

