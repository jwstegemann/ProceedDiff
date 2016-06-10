package org.proceed.tree

import scala.collection.mutable

/**
  * Created by tiberius on 08.06.16.
  */
trait Node {

  val children = new mutable.LinkedHashMap[String, Node]

}
