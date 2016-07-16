package proceed.util


import scala.collection.mutable

/**
  * Classnames
  *
  * @author Jan Weidenhaupt
  *         13.07.2016
  */
case class ClassName(list: mutable.HashSet[String]) {
  override def toString: String = list.mkString(" ")

  def size = list.size

  def +(s: String): ClassName = {
    list += s
    this
  }

  def ++(s: String*) = {
    list ++= s
    this
  }

  def -(s: String) = {
    list -= s
    this
  }

  def --(s: String*) = {
    list --= s
    this
  }

  def filter(f: (String) => Boolean) = {
    ClassName(list.filter(f))
  }
}

object ClassName {
  def apply(s: String*): ClassName = {
    ClassName(mutable.HashSet.empty[String] ++ s.filterNot(_ == "").view)
  }

  def empty: ClassName = {
    ClassName(mutable.HashSet.empty[String])
  }
}

