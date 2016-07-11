package proceed.diff

import proceed.diff.patch.PatchQueue
import proceed.tree.{Component, Element}
import proceed.util.log

import scala.collection.mutable

/**
  * Created by tiberius on 29.06.16.
  */


case class RenderItem(component: Component, parent: Element, sibbling: Option[Element], patchQueue: PatchQueue) {
  lazy val depth = component.path.count(_ == '.')

  def render(queue: RenderQueue) = {
    component.render(patchQueue, parent, sibbling, queue)
  }
}

object RenderQueue {

  implicit val ordering = new Ordering[RenderItem] {
    override def compare(x: RenderItem, y: RenderItem): Int = {
      x.depth.compare(y.depth)
    }
  }

}

class RenderQueue extends mutable.PriorityQueue[RenderItem]()(RenderQueue.ordering) {

  implicit val queue = this

  def enqueue(elem: RenderItem): PatchQueue = {
    super.enqueue(elem)
    elem.patchQueue
  }

  def renderNext() = {
    dequeue().render(this)
  }

}
