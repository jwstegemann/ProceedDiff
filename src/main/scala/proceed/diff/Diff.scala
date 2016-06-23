package proceed.diff

import patch.Patch
import proceed.tree.{Component, Element, Node}

import scala.collection.mutable.LinkedHashMap

/**
  * Created by tiberius on 10.06.16.
  */
object Diff {

  //TODO: implement diff of two nodes
  def diff(oldNode: Node, newNode: Node) : Seq[Patch] = {

    (oldNode, newNode) match {
      case (oldElement: Element, newElement: Element) => ""
      case (oldComponent: Component, newComponent: Component) => ""
     // case (oldComponent: Component, Empty)
    }

    Nil
  }

  def diff(left: LinkedHashMap[String, Node], right: LinkedHashMap[String, Node]) : Seq[Patch] = {

    val rightIterator = right.iterator
    val leftIterator = left.iterator


    Nil

  }

}
