package proceed.diff

import proceed.App
import proceed.diff.patch._
import proceed.tree._
import proceed.util.{ClassName, log}

import scala.annotation.tailrec

object Diff {

  // TODO: write macro for this
  def compareAndPatchAttributes(oldElement: Element, newElement: Element, patchQueue: PatchQueue): Unit = {
    for ((name, (oldValue, newValue)) <- oldElement.fields.iterator.zip(oldElement.iterator.zip(newElement.iterator))) {
      if (oldValue != newValue) {
        newValue match {
          case None => patchQueue.enqueue(RemoveAttribute(newElement, name))
          case optionalValue: Some[_] => patchQueue.enqueue(SetAttribute(newElement, name, optionalValue.get.toString))
          case className: ClassName => patchQueue.enqueue(SetClassName(newElement, className))
          case value => patchQueue.enqueue(SetAttribute(newElement, name, value.toString))
        }
      }
    }
  }

  // reuse node at the same place (id stays the same)
  def reuse(parent: Element, oldNode: Node, newNode: Node, patchQueue: PatchQueue) = {
    (oldNode, newNode) match {
      case (oldElement: Element, newElement: Element) => {
        newElement.domRef = oldElement.domRef
        compareAndPatchAttributes(oldElement, newElement, patchQueue)
        // continue comparing children
        diff(oldElement.children, newElement.children, newElement.childrensPath, newElement, patchQueue)
      }
      case (oldComponent: Component, newComponent: Component) => {
        //FIXME: extract into method in Component
        newComponent.takeChildrenFrom(oldComponent)
        newComponent.durable = oldComponent.durable
        newComponent.durable.transient = newComponent

        //FIXME: resort Statefull vs. Not
        (oldComponent, newComponent) match {
          case (oc: StatefullComponent[Product], nc: StatefullComponent[Product]) => {
            if (oc != nc) {
              //TODO: same as for non-statefull
              nc.parametersChanged()
              patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(nc.durable, parent, None, new PatchQueue)))
            }
            else {
              val oldState = oc.state
              //FIXME: overwrite method created above to do this
              nc.state = oc.state

              if (nc.dirty || nc.shouldRender(oldState)) {
                  patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(nc.durable, parent, None, new PatchQueue)))
              }
            }
          }
          case (oc, nc) => {
            if (oc != nc) patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(nc.durable, parent, None, new PatchQueue)))
          }
        }
      }
    }
  }

  // move node to another place and reuse (ChildMap.key changes)
  def move(parent: Element, oldNode: Node, newNode: Node, sibbling: Option[Node], patchQueue: PatchQueue) = {
    patchQueue.enqueue(MoveChild(parent, newNode.element, sibbling))
    reuse(parent, oldNode, newNode, patchQueue)
  }

  def insertOrAppendNew(path: String, parent: Element, node: Node, sibbling: Option[Node], patchQueue: PatchQueue) = {
    node match {
      case element: Element => {
        patchQueue.enqueue(CreateNewChild(parent, element, sibbling))
        diff(NoChildsMap, element.children, element.childrensPath, element, patchQueue)
      }
        //FIXME: Difference between Statefull and not here?
      case component: StatefullComponent[Product] => {
        component.parent = parent
        component.prepare()
        patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(component.durable, parent, sibbling.map(s => s.element), new PatchQueue)))
      }
      case component: Component => {
        component.parent = parent
        component.prepare()
        //FIXME: Render StatelessComponents directly?
        patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(component.durable, parent, sibbling.map(s => s.element), new PatchQueue)))
      }
    }
  }

  def delete(parent: Element, node: Node, patchQueue: PatchQueue) = {
    patchQueue.enqueue(DeleteChild(parent, node.element))
    node.traverseComponents(_.remove())
  }

  def diff(oldList: ChildMap, newList: ChildMap, path: String, parentElement: Element, patchQueue: PatchQueue) : Unit = {

    val oldIterator = oldList.iterate()
    val newIterator = newList.iterate()

    while (!(newIterator.done && oldIterator.done)) {
      newIterator.currentItem.path = path

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
