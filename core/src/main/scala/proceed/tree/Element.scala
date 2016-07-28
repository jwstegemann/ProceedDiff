package proceed.tree

import org.scalajs.dom
import org.scalajs.dom.raw
import proceed.tree.html.TextNode

/**
  * Element
  *
  * @author Jan Weidenhaupt
  * @since 1.0
  */
abstract class Element extends DomNode {
  p: Product =>

  def apply(c: Node, cs: Node*): Element = {
    apply(c +: cs)
    this
  }

  def apply(cs: Seq[Node]): Element = {
    children = new ChildMapImpl
    cs.zipWithIndex.foreach {
      case (n: Node, i: Int) => children.add(i, n)
    }
    this
  }

  def apply(): Element = {
    this
  }

  def createDomRef() = {
    val newDomElement = dom.document.createElement(this.nodeType)
    if(this.key.isDefined) newDomElement.id = this.key.get
    newDomElement.setAttribute("data-proceed",this.childrensPath)
    this.domRef = Some(newDomElement.asInstanceOf[DomNodeRefType])
  }

  def domNode = this
}
