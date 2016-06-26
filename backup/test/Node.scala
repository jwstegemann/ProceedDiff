package test

import scala.collection.mutable

/**
  * Created by tiberius on 08.06.16.
  */
trait Node {

  val children = new mutable.LinkedHashMap[String, Node]

  val xtype = getClass.hashCode

  def apply(nodes: Node*) = {
    nodes.foreach(n => println(n))
    this
  }

}
