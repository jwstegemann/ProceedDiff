package proceed.diff

import proceed.tree.Component

import scala.collection.mutable

/**
  * Created by tiberius on 29.06.16.
  */

object RenderQueue {

  implicit val ordering = new Ordering[Component] {
    override def compare(x: Component, y: Component): Int = {
      x.path.length.compare(y.path.length)
    }
  }
}

class RenderQueue(implicit ordering: Ordering[Component]) extends mutable.PriorityQueue[Component] {


}
