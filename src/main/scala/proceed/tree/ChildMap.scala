package proceed.tree

import scala.collection.mutable

trait ChildMap {
  def add(position: Int, node: Node): Unit
  def indexOf(key: String): Option[(Int, Node)]
  def iterate(): ChildIterator
  def getFirstChild() : Node
  def getNode(key: String) : Option[Node]
}

object ChildMap {
  def apply(element: Element) = {
    val children = new ChildMapImpl()
    children.add(0,element)
    children
  }
}

trait ChildIterator {

  var done: Boolean = false

  def continue(): Unit
  def currentPos(): Int
  def currentItem(): Node
  def lastItem(): Option[Node]
  def currentKey(): String

  //TODO: is it still needed?
  def replaceCurrentItem(newNode: Node)

}

/**
  * Created by tiberius on 23.06.16.
  */
class ChildMapImpl extends mutable.LinkedHashMap[String, (Int, Node)] with ChildMap {

  override def getFirstChild(): Node = firstEntry.value._2

  //FIXME: keep track of position in Iterator
  def add(position: Int, node: Node): Unit = {

    node.id = node.key match {
      case None => s"${node.nodeType}$position"
      case Some(key) => key
    }
    put(node.id, (position, node))
  }

  override def getNode(key: String) = get(key).map(_._2)

  def indexOf(key: String): Option[(Int, Node)] = {
    get(key)
  }

  def iterate() = new ChildIteratorImpl(reversed.iterator)


  class ChildIteratorImpl(val mapIterator: Iterator[(String, (Int, Node))]) extends ChildIterator {

    var currentChild:(String, (Int, Node)) = mapIterator.next()
    var lastChild:Option[(String, (Int, Node))] = None

    done = false

    def continue() = {
      lastChild = Some(currentChild)
      if (mapIterator.hasNext) {
        currentChild = mapIterator.next()
      }
      else {
        done = true
        currentChild = ("EmptyNode:-1",(-1,EmptyNode))
      }
    }

    def currentPos() = currentChild._2._1
    def currentItem() = currentChild._2._2
    def currentKey() = currentChild._1

    def lastItem():Option[Node]  = lastChild.map((child: (String,(Int,Node))) => child._2._2)

    def replaceCurrentItem(newNode: Node) = {
      ChildMapImpl.this.put(currentKey, (currentPos, newNode))
      currentChild = (currentKey, (currentPos, newNode))
    }

  }

}


object NoChildsMap extends ChildMap {

  override def add(position: Int, node: Node) = throw new UnsupportedOperationException

  override def getNode(key: String) = None

  override def indexOf(key: String) = None
  override def iterate() = new EmptyChildIterator()
  override def getFirstChild(): Node = EmptyNode

  class EmptyChildIterator extends ChildIterator() {

    done = true

    override def currentPos(): Int = -1
    override def currentKey(): String = EmptyNode.key.get
    override def lastItem(): Option[Node] = None
    override def currentItem(): Node = EmptyNode
    override def continue(): Unit = {}
    override def replaceCurrentItem(newNode: Node) = throw new UnsupportedOperationException
  }

}


