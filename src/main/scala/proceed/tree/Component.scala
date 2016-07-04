package proceed.tree

import proceed.diff.Diff
import proceed.diff.patch.PatchQueue

/**
  * Created by tiberius on 17.06.16.
  */
abstract class Component extends Node {

  var dirty = false

  var parent: Element = _

  def view(): Element

  final def render(patchQueue: PatchQueue, parentElement: Element, sibling: Option[Element] ): Unit = {
    val newChildren = ChildMap(view())

    Diff.diff(children, newChildren, parentElement, patchQueue)

    children = newChildren

  }

  //FIXME: better without casting? generic ChildMap?
  override def element: Element = children.getFirstChild().asInstanceOf[Element]


  /*
   * lifecycle-hooks
   */
  def isRemoved(): Unit = {}
  def parametersChanged() : Unit = {}
  //FIXME: compare state here
  def shouldRender() : Boolean = true
}
