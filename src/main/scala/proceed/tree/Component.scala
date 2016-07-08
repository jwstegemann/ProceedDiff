package proceed.tree

import proceed.diff.Diff
import proceed.diff.patch.PatchQueue
import proceed.events.EventHandler

/**
  * Created by tiberius on 17.06.16.
  */
abstract class Component extends Node with EventHandler {

  var dirty = false
  var parent: Element = _
  implicit val self: Component = this // is used by all apply-calls to transfer owner

  def view(): Element

  final def render(patchQueue: PatchQueue, parentElement: Element, sibling: Option[Element] ): Unit = {
//    implicit val owner = this
    val child = view()
    child.key = Some("0")
    val newChildren = ChildMap(child)
    //FIXME: use RenderQueue
    Diff.diff(children, newChildren, s"$path.$id", parentElement, patchQueue)
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

    //TODO: create special ChildMap for one Element-child
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
    Diff.diff(children, NoChildsMap, s"$path.$id", parent.element, patchQueue)
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

abstract class StatefullComponent[T <: Product] extends Component {
  product: Product =>

  var state: T = initialState()

  def setState(newState: T) = {
    state = newState
    dirty = true
  }

  /*
   * lifecycle-hooks
   */
  def initialState(): T
}
