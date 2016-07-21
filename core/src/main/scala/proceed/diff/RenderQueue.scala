package proceed.diff

import proceed.diff.patch.PatchQueue
import proceed.tree.{DurableLink, Element}
import proceed.util.log

import scala.collection.mutable


case class RenderItem(component: DurableLink, parent: Element, sibbling: Option[Element], patchQueue: PatchQueue) {
  lazy val depth = component.transient.path.count(_ == '.')

  def render(queue: RenderQueue) = {
    component.transient.render(patchQueue, parent, sibbling)
  }
}

object RenderQueue {

  implicit val ordering = new Ordering[RenderItem] {
    override def compare(x: RenderItem, y: RenderItem): Int = x.depth - y.depth
  }
}


class RenderQueue extends mutable.PriorityQueue[RenderItem]()(RenderQueue.ordering) {

  def enqueue(item: RenderItem): PatchQueue = {
    if (!item.component.enqued) {
      super.enqueue(item)
      item.component.enqued = true
      log.debug(s"enqued component ${item.component.transient} of ${this.length}")
    }
    item.patchQueue
  }

  def renderNext() = {
    val item = dequeue()
    item.component.enqued = false
    if (!item.component.invalid) item.render(this)
    else log.debug(s"removed invalid component ${item.component} from render-queue")
  }

}
