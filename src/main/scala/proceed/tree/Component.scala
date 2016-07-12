package proceed.tree

import proceed.diff.{Diff, RenderQueue}
import proceed.diff.patch.PatchQueue
import proceed.events.EventHandler
import proceed.util.log

/**
  * Created by tiberius on 17.06.16.
  */
abstract class Component extends Node {

  var dirty = false
  var parent: Element = _
  implicit val self: Component = this // is used by all apply-calls to transfer owner

  def view(): Element

  final def render(patchQueue: PatchQueue, parentElement: Element, sibling: Option[Element], renderQueue: RenderQueue): Unit = {
//    implicit val owner = this
    val child = view()
    child.key = Some("0")
    val newChildren = ChildMap(child)
    //FIXME: first child has always to be reused and never created new (since sibbling is unknown) Warning?
    Diff.diff(children, newChildren, s"$path.$id", parentElement, patchQueue, renderQueue)
    children = newChildren
    dirty = false
  }

  //TODO: better without casting? generic ChildMap?
  override def element: Element = children.getFirstChild().asInstanceOf[Element]

  override def getNewHandlingComponent(c: Option[Component]) = Some(this)

  def takeChildrenFrom(other: Component) = {
    this.children = other.children
  }

  def mount(mp: MountPoint): Unit = {
    path = mp.id
    parent = mp

    //TODO: create special ChildMap for one Element-child
    mp.children = new ChildMapImpl
    mp.children.add(0, this)

    mp.eventLoop((rq, pq) => render(pq, mp, None, rq))
  }

  def mount(domId: String): Unit = {
    mount(MountPoint(domId))
  }

  def unmount(): Unit = {
    willUnmount()
    parent match {
      case mp: MountPoint => mp.eventLoop((rq, pq) => Diff.diff(children, NoChildsMap, s"$path.$id", parent.element, pq, rq))
      case _ => log.error(s"Component $this is not mounted and cannot be unmounted therefore.")
    }
  }

  def remove(): Unit = {}

  /*
   * lifecycle-hooks
   */

  def isRemoved(): Unit = {}
  def willUnmount(): Unit = {}
}

abstract class StatefullComponent[T <: Product] extends Component {
  product: Product =>

  var state: T = initialState()

  def setState(newState: T) = {
    state = newState
    dirty = true
  }

  override def remove(): Unit = {
    //FIXME: deregister component
  }


  /*
   * lifecycle-hooks
   */

  def initialState(): T
  def parametersChanged() : Unit = {}
  def shouldRender(oldState: T) = (state != oldState) //TODO: deep compare

}
