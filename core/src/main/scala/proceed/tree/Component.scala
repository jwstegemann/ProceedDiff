package proceed.tree

import scala.language.experimental.macros
import proceed.{App, DataBindingMacros}
import proceed.store.{Store, Subscriber}
import proceed.diff.{Diff, RenderItem}
import proceed.diff.patch.PatchQueue
import proceed.events.{EventDelegate, EventType, Input}
import proceed.util.log

/**
  * Created by tiberius on 17.06.16.
  */
abstract class Component extends Node with EventDelegate {

  var dirty = false
  var parent: Element = _

  var durable: DurableLink = _

//  implicit def unbindEventHandler[T <: EventType](m: (this.type, T) => Any): (this.type, T)

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

  def adopt(other: Component): Unit = {
    children = other.children
    durable = other.durable
    durable.transient = this
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

    App.startEventLoop((pq) => render(pq, mp, None))
  }

  def mount(domId: String): Unit = {
    mount(MountPoint(domId))
  }

  def unmount(): Unit = {
    parent match {
      case mp: MountPoint => App.startEventLoop((pq) => Diff.diff(children, NoChildsMap, s"$path.$id", parent.element, pq))
      case _ => log.error(s"Component $this is not mounted and cannot be unmounted therefore.")
    }
  }

  def remove(): Unit = {
    durable.unsubscribeAll()
    durable.invalid = true
  }

  def subscribe(Store: Store): Boolean = durable.subscribe(Store)
  def unsubscribe(Store: Store): Boolean = durable.unsubscribe(Store)

  /*
   * lifecycle-hooks
   */

  def isRemoved(): Unit = {}
  def init(): Unit = {}
}


class DurableLink(var transient: Component) extends Subscriber {
  var invalid = false
  var enqued = false

  override def receive(patchQueue: PatchQueue) = {
    transient.dirty = true
    log.debug(s"${this.transient} is receiving")
    App.renderQueue.enqueue(new RenderItem(this, transient.parent, None, patchQueue))
  }
}

abstract class StatefullComponent[T <: Product] extends Component {
  product: Product =>

  var state: T = initialState()

  def setState(newState: T) = {
    this.state = newState
    dirty = true
  }

  /*
   * lifecycle-hookss
   */

  def initialState(): T
  def parametersChanged() : Unit = {}
  def shouldRender(oldState: T) = state != oldState //TODO: deep compare
}

trait DataBinding[T <: Product] {
  self: StatefullComponent[T] =>

  def bind[U, E <: EventType](eventType: E)(path: T => U)(implicit value: eventType.Event => U): (E, Any) = macro DataBindingMacros.bindImpl[T,U,E]

  def update[U](path: T => U, value: U): Unit = macro DataBindingMacros.updateImpl[T,U]

}
