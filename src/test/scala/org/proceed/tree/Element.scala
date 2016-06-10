package org.proceed.tree

import scala.collection.immutable.HashMap

/**
  * Created by tiberius on 08.06.16.
  */
class Element extends Node {

  var properties: Map[String, String] = new HashMap[String, String]

}
