package proceed.tree

import org.scalajs.dom
import org.scalajs.dom.raw

/**
  * Element
  *
  * @author Jan Weidenhaupt
  * @since 1.0
  */
trait Element extends DomNode {
  p: Product =>

  type DomNodeRefType = raw.Element

  def apply(c: DomNode, cs: DomNode*): DomNode = {
    apply(c +: cs)
    this
  }

  def apply(cs: Seq[DomNode]): DomNode = {
    children = new ChildMapImpl
    cs.zipWithIndex.foreach {
      case (n: Node, i: Int) => children.add(i, n)
    }
    this
  }

  def apply(): DomNode = {
    this
  }

  def createDomRef() = {
    val newDomElement = dom.document.createElement(this.nodeType)
    if(this.key.isDefined) newDomElement.id = this.key.get
    newDomElement.setAttribute("data-proceed",this.childrensPath)
    this.domRef = Some(newDomElement)
  }

  def domNode = this
}
