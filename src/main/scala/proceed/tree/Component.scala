package proceed.tree

import proceed.App
import proceed.actions.{Publisher, ReRender, Receiver, Subscriber}
import proceed.diff.{Diff, RenderQueue}
import proceed.diff.patch.PatchQueue
import proceed.util.log

/**
  * Created by tiberius on 17.06.16.
  */
abstract class Component extends Node {

  var dirty = false
  var parent: Element = _

  var durable: DurableLink = _

  implicit val self: Component = this // is used by all apply-calls to transfer owner

  def view(): Element

  final def render(patchQueue: PatchQueue, parentElement: Element, sibling: Option[Element]): Unit = {
    //    implicit val owner = this
    val child = view()
    child.key = Some("0")
    val newChildren = ChildMap(child)
    //FIXME: first child has always to be reused and never created new (since sibbling is unknown) Warning?
    Diff.diff(children, newChildren, s"$path.$id", parentElement, patchQueue)
    children = newChildren
    dirty = false
  }

  //TODO: better without casting? generic ChildMap?
  override def element: Element = children.getFirstChild().asInstanceOf[Element]

  override def getNewHandlingComponent(c: Option[Component]) = Some(this)

  def takeChildrenFrom(other: Component) = {
    this.children = other.children
  }

  def prepare(): Unit = {
    log.debug(s"preparing component $this")
    durable = new DurableLink(this)
    init()
  }

  def mount(mp: MountPoint): Unit = {
    path = mp.id
    parent = mp

    prepare()

    //TODO: create special ChildMap for one Element-child
    mp.children = new ChildMapImpl
    mp.children.add(0, this)

    App.eventLoop((pq) => render(pq, mp, None))
  }

  def mount(domId: String): Unit = {
    mount(MountPoint(domId))
  }

  def unmount(): Unit = {
    parent match {
      case mp: MountPoint => App.eventLoop((pq) => Diff.diff(children, NoChildsMap, s"$path.$id", parent.element, pq))
      case _ => log.error(s"Component $this is not mounted and cannot be unmounted therefore.")
    }
  }

  def remove(): Unit = {}

  /*
   * Messaging
   */

  def dispatch: PartialFunction[Product, Unit] = PartialFunction.empty

  /*
   * lifecycle-hooks
   */

  def isRemoved(): Unit = {}
  def init(): Unit = {}
}


class DurableLink(var transient: Component) extends Subscriber {

  override def dispatch = {
    case x => transient.dispatch(x)
  }

  var invalid = false
  var enqued = false
}

abstract class StatefullComponent[T <: Product] extends Component {
  product: Product =>

  var state: T = initialState()

  def setState(newState: T) = {
    this.state = newState
    dirty = true
  }

  override def remove(): Unit = {
    durable.unsubscribeAll()
    durable.invalid = true
  }

  def subscribe(publisher: Publisher): Boolean = durable.subscribe(publisher)
  def unsubscribe(publisher: Publisher): Boolean = durable.unsubscribe(publisher)

  /*
   * lifecycle-hooks
   */

  def initialState(): T
  def parametersChanged() : Unit = {}
  def shouldRender(oldState: T) = state != oldState //TODO: deep compare
}
