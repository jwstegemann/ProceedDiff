package proceed.diff

import proceed.tree.Component

import scala.collection.mutable

/**
  * Created by tiberius on 29.06.16.
  */


object RenderQueue {

  implicit val ordering = new Ordering[Component] {
    override def compare(x: Component, y: Component): Int = {
      x.path.count(_ == '.').compare(y.path.count(_ == '.'))
    }
  }

}

class RenderQueue extends mutable.PriorityQueue[Component]()(RenderQueue.ordering) {

  def enqueue(elem: Component): Unit = {
    println(s"enqueing $elem")
    super.enqueue(elem)
  }
}
