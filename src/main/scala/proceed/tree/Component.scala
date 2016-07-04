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
    Diff.diff(children, newChildren, path, parentElement, patchQueue)
    children = newChildren
  }

  //TODO: better without casting? generic ChildMap?
  override def element: Element = children.getFirstChild().asInstanceOf[Element]

  def takeChildrenFrom(other: Component) = {
    this.children = other.children
  }

  def mount(mp: MountPoint): Unit = {
    path = mp.id
    parent = mp

    mp.children = new ChildMapImpl
    mp.children.add(0, this)

    val patchQueue = new PatchQueue()
    render(patchQueue, mp, None)
    patchQueue.execute()
  }

  def mount(domId: String): Unit = {
    mount(MountPoint(domId))
  }

  def unmount(): Unit = {
    willUnmount()
    val patchQueue = new PatchQueue()
    Diff.diff(children, NoChildsMap, path, parent.element, patchQueue)
    patchQueue.execute()
  }

  /*
   * lifecycle-hooks
   */
  def isRemoved(): Unit = {}
  def parametersChanged() : Unit = {}
  def willUnmount(): Unit = {}
  //FIXME: compare state here
  def shouldRender() : Boolean = true

}
