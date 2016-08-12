package proceed.diff

import proceed.App
import proceed.diff.patch._
import proceed.tree._
import proceed.tree.html.TextNode

import scalacss.StyleA

object Diff {

  def compareAndPatchAttributes(oldElement: Element, newElement: Element, patchQueue: PatchQueue): Unit = {
    for ((name, (oldValue, newValue)) <- oldElement.fields.iterator.zip(oldElement.iterator.zip(newElement.iterator))) {
      if (oldValue != newValue) {
        newValue match {
          case None => patchQueue.enqueue(RemoveAttribute(newElement, name))
          case Some(className: StyleA) => patchQueue.enqueue(SetClassName(newElement, className))
          case optionalValue: Some[_] => patchQueue.enqueue(SetAttribute(newElement, name, optionalValue.get.toString))
          case value => patchQueue.enqueue(SetAttribute(newElement, name, value.toString))
        }
      }
    }
  }

  def patchAttributes(newElement: DomNode, patchQueue: PatchQueue): Unit = {
    for ((name, newValue) <- newElement.fields.iterator.zip(newElement.iterator)) {
      newValue match {
        case None =>
        case Some(className: StyleA) => patchQueue.enqueue(SetClassName(newElement, className))
        case optionalValue: Some[_] => patchQueue.enqueue(SetAttribute(newElement, name, optionalValue.get.toString))
        case value => patchQueue.enqueue(SetAttribute(newElement, name, value.toString))
      }
    }
  }

  // reuse node at the same place (id stays the same)
  def reuse(parent: DomNode, oldNode: Node, newNode: Node, patchQueue: PatchQueue) = {
    (oldNode, newNode) match {
      case (oldText: TextNode, newText: TextNode) => {
        newText.domRef = oldText.domRef
        if (oldText.content != newText.content) patchQueue.enqueue(ChangeText(newText, parent, newText.content))
      }
      case (oldElement: Element, newElement: Element) => {
        //TODO: get rid of cast?
        newElement.domRef = oldElement.domRef.asInstanceOf[Option[newElement.DomNodeRefType]]
        compareAndPatchAttributes(oldElement, newElement, patchQueue)
        // continue comparing children
        diff(oldElement.children, newElement.children, newElement.childrensPath, newElement, patchQueue)
      }
      case (oldComponent: StatefullComponent[Product], newComponent: StatefullComponent[Product]) => {
        newComponent.adopt(oldComponent)

        newComponent.state = oldComponent.state

        if (oldComponent != newComponent) {
          newComponent.parametersChanged()
          patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(newComponent.durable, parent, None, new PatchQueue)))
        }
/*
        else {
          //FIXME: was this necessary?
          if (newComponent.dirty || newComponent.shouldRender(oldState)) {
            patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(newComponent.durable, parent, None, new PatchQueue)))
          }
        }
*/
      }
      case (oldComponent: Component, newComponent: Component) => {
        newComponent.adopt(oldComponent)
        if (oldComponent != newComponent) {
          patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(newComponent.durable, parent, None, new PatchQueue)))
        }
      }
    }
  }

  // move node to another place and reuse (ChildMap.key changes)
  def move(parent: DomNode, oldNode: Node, newNode: Node, sibbling: Option[Node], patchQueue: PatchQueue) = {
    patchQueue.enqueue(MoveChild(parent, newNode.domNode, sibbling))
    reuse(parent, oldNode, newNode, patchQueue)
  }

  def insertOrAppendNew(path: String, parent: DomNode, node: Node, sibbling: Option[Node], patchQueue: PatchQueue) = {
    node match {
      case element: DomNode => {
        patchQueue.enqueue(CreateNewChild(parent, element, sibbling))
        patchAttributes(element, patchQueue)
        diff(NoChildsMap, element.children, element.childrensPath, element, patchQueue)
      }
      case component: Component => {
        component.parent = parent
        component.prepare()
        patchQueue.enqueue(App.renderQueue.enqueue(RenderItem(component.durable, parent, sibbling.map(s => s.domNode), new PatchQueue)))
      }
    }
  }

  def delete(parent: DomNode, node: Node, patchQueue: PatchQueue) = {
    patchQueue.enqueue(DeleteChild(parent, node.domNode))
    node.traverseComponents(_.remove())
  }

  def diff(oldList: ChildMap, newList: ChildMap, path: String, parentElement: DomNode, patchQueue: PatchQueue) : Unit = {

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
