package proceed.diff

import proceed.diff.patch._
import proceed.tree._

/**
  * Created by tiberius on 10.06.16.
  */
object Diff {

  // TODO: write macro for this
  def compareAndPatchAttributes(oldElement: Element, newElement: Element, patchQueue: PatchQueue): Unit = {
    for ((name, (oldValue, newValue)) <- oldElement.fields.iterator.zip(oldElement.iterator.zip(newElement.iterator))) {
      if (oldValue != newValue) {
        newValue match {
          case None => patchQueue.enqueue(RemoveAttribute(newElement, name))
          case optionalValue: Some[_] => patchQueue.enqueue(SetAttribute(newElement, name, optionalValue.get.toString))
          case value => patchQueue.enqueue(SetAttribute(newElement, name, value.toString))
        }
      }
    }
  }

  // reuse node at the same place (id stays the same)
  def reuse(parent: Element, oldNode: Node, newNode: Node, patchQueue: PatchQueue) = {
    (oldNode, newNode) match {
      case (oldElement: Element, newElement: Element) => {
        println("reuse Element")
        compareAndPatchAttributes(oldElement, newElement, patchQueue)
        // continue comparing children
        //TODO: better non-recursive
        diff(oldElement.children, newElement.children, newElement.childrensPath, newElement, patchQueue)
      }
      case (oldComponent: Component, newComponent: Component) => {
        println("reuse Component")

        newComponent.takeChildrenFrom(oldComponent)

        if (oldComponent != newComponent) {
          newComponent.parametersChanged()
          if (newComponent.shouldRender()) newComponent.render(patchQueue, parent, None)
        }
      }
    }
  }

  // move node to another place and reuse (ChildMap.key changes)
  def move(parent: Element, oldNode: Node, newNode: Node, sibbling: Option[Node], patchQueue: PatchQueue) = {
    patchQueue.enqueue(MoveChild(parent, newNode.element, sibbling))
    //FIXME: this might result an the deletion of the just created node (for components)
    reuse(parent, oldNode, newNode, patchQueue)
  }

  def insertOrAppendNew(path: String, parent: Element, node: Node, sibbling: Option[Node], patchQueue: PatchQueue) = {
    node match {
      case element: Element => {
        patchQueue.enqueue(CreateNewChild(parent, element, sibbling))
        diff(NoChildsMap, element.children, element.childrensPath, element, patchQueue)
      }
      case component: Component => {
        component.parent = parent
        component.render(patchQueue, parent, sibbling.map(s => s.element))
      }
    }
  }

  def delete(parent: Element, node: Node, patchQueue: PatchQueue) = {
    node match {
      case element: Element => patchQueue.enqueue(DeleteChild(parent, element))
      case component: Component => {
        component.isRemoved()
        patchQueue.enqueue(DeleteChild(parent, component.element))
      }
    }
  }

  //FIXME: is parent needed here
  def diff(oldList: ChildMap, newList: ChildMap, path: String, parentElement: Element, patchQueue: PatchQueue) : Unit = {

    val oldIterator = oldList.iterate()
    val newIterator = newList.iterate()

    while (!(newIterator.done && oldIterator.done)) {
      newIterator.currentItem.path = path

      println("comparing old(" + oldIterator.currentKey + ") with new(" + newIterator.currentKey + ")")

      // reuse if same type at same position
      if (oldIterator.currentKey == newIterator.currentKey) {

        reuse(parentElement, oldIterator.currentItem, newIterator.currentItem, patchQueue)

        oldIterator.continue()
        newIterator.continue()
      }
      // insert new node
      else if (!newIterator.done && oldList.indexOf(newIterator.currentKey).isEmpty) {
        insertOrAppendNew(path, parentElement, newIterator.currentItem, newIterator.lastItem, patchQueue)
        newIterator.continue()

        // delete old nodes that are not needed anymore in the same step
        if (oldIterator.currentItem.key.isEmpty) {
          delete(parentElement, oldIterator.currentItem, patchQueue)
          oldIterator.continue()
        }
      }
      //TODO: is checking für oldIterator here right?
      else if (!oldIterator.done) {
        // move node if possible (key is implicitly present)
        newList.indexOf(oldIterator.currentKey) match {
          case Some((pos: Int, node: Node)) => {
            if (pos <= newIterator.currentPos) {
              move(parentElement, oldIterator.currentItem, newIterator.currentItem, newIterator.lastItem, patchQueue)
              newIterator.continue()
            } else {
              oldIterator.continue()
            }
          }
          // delete if it is finally not needed
          case None => {
            delete(parentElement, oldIterator.currentItem, patchQueue)
            oldIterator.continue()
          }
        }
      }

    }

  }

}
