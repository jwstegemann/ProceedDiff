package proceed.diff

import patch.{InsertNewChild, Patch, PatchQueue}
import proceed.tree.{ChildMap, Component, Element, Node}

import scala.collection.mutable.LinkedHashMap

/**
  * Created by tiberius on 10.06.16.
  */
object Diff {

  def reuseElement() = {

  }

  def reuseComponent() = {

  }

  def reuseAndMoveComponent() = {

  }

  def reuseAndMoveElement() = {

  }

  def insertNewElement() = {
    patchQueue.enqueue(
      InsertNewChild(parentElement, newIterator.currentItem.element, beforeItem.element))
  }

  def appendNewElement() = {
    println("# append " + newIterator.currentItem)

  }

  def insertNewComponent() = {

  }

  def appendNewComponent() = {

  }

  def deleteElement() = {

  }

  def deleteComponent() = {

  }

  //FIXME: is parent needed here
  def diff(oldList: ChildMap, newList: ChildMap, parentElement: Element, patchQueue: PatchQueue) : Unit = {

    val oldIterator = oldList.iterate
    val newIterator = newList.iterate

    while (!(newIterator.done && oldIterator.done)) {

      println("comparing old(" + oldIterator.currentKey + ") with new(" + newIterator.currentKey + ")")

      if (oldIterator.currentKey == newIterator.currentKey) {

        println("...reuse " + oldIterator.currentItem + " for " + newIterator.currentItem)

        (oldIterator.currentItem, newIterator.currentItem) match {
          case (oldElement:Element, newElement: Element) => reuseElement()
          case (oldComponent: Component, newComponent: Component) => reuseComponent()
        }
        oldIterator.continue()
        newIterator.continue()
      }
      if (!newIterator.done && oldList.indexOf(newIterator.currentKey()).isEmpty) {
        (newIterator.currentItem, newIterator.lastItem) match {
          // # create " + newIterator.currentItem + " before " + beforeItem)
          case (element: Element, Some(beforeItem: Node)) => insertNewElement()
          case (element: Element, None) => appendNewElement()
          case (component: Component, Some(beforeItem: Node)) => insertNewComponent()
          case (component: Component, None) => appendNewComponent()
        }
        newIterator.continue()

        if (oldIterator.currentItem().key.isEmpty) {
          oldIterator.currentItem match {
            case component: Component => deleteComponent(parentElement, component)
            case element: Element => deleteElement(parentElement, element)
          }
          oldIterator.continue()
        }
      }
      //TODO: is checking für oldIterator here right?
      else if (!oldIterator.done) {
        newList.indexOf(oldIterator.currentKey()) match {
          case Some((pos: Int, node: Node)) => {
            if (pos <= newIterator.currentPos()) {
              newIterator.lastItem match {
                case Some(beforeItem: Node) => println("# move " + newIterator.currentItem + " before " + beforeItem)
                case None => println("# move " + newIterator.currentKey + " to end")
              }
              newIterator.continue()
            } else {
              oldIterator.continue()
            }
          }
          case None => {
            println("# delete " + oldIterator.currentItem)
            oldIterator.continue()
          }
        }
      }

    }

  }

}
