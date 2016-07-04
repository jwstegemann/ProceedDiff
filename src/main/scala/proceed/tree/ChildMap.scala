package proceed.tree

import scala.collection.mutable

trait ChildMap {
  def add(position: Int, node: Node): Unit
  def indexOf(key: String): Option[(Int, Node)]
  def iterate(): ChildIterator
  def getFirstChild() : Node
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

}

/**
  * Created by tiberius on 23.06.16.
  */
class ChildMapImpl extends mutable.LinkedHashMap[String, (Int, Node)] with ChildMap {

  override def getFirstChild(): Node = firstEntry.value._2

  def add(position: Int, node: Node): Unit = {
    //setting the id on the node when added to a parent node
    node.id = node.key.getOrElse(position.toString)

    // TODO: reuse instance of StringBuilder?
    val mapKey = mutable.StringBuilder.newBuilder
        .append(node.nodeType)
        .append(":")
        .append(node.id)
        .toString
    put(mapKey, (position, node))

/*  // TODO: decide... to get keys as short as possible (type is not needed when key is present)
    if (node.key.isEmpty) {
      node.id = position.toString
      put(node.nodeType + ":" + node.id, (position, node))
    } else {
      node.id = node.key.get
      put(node.id, (position, node))
    }
*/

  }

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

  }

}


object NoChildsMap extends ChildMap {

  def add(position: Int, node: Node) = {
    throw new UnsupportedOperationException
  }

  override def indexOf(key: String) = None
  override def iterate() = new EmptyChildIterator()
  override def getFirstChild(): Node = EmptyNode

  class EmptyChildIterator extends ChildIterator() {

    done = true

    def currentPos(): Int = -1
    def currentKey(): String = EmptyNode.key.get
    def lastItem(): Option[Node] = None
    def currentItem(): Node = EmptyNode
    def continue(): Unit = {}

  }

}


