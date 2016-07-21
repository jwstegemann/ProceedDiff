package proceed.tree

import scala.collection.mutable

/**
  * Classnames
  *
  * holds all specified classNames in a mutable.HashSet
  * and present some methods for adding or removing classNames
  *
  * @author Jan Weidenhaupt
  *         13.07.2016
  */
sealed case class ClassName(private final val list: mutable.HashSet[String]) {

  override def toString: String = list.mkString(" ")

  lazy val size: Int = list.size

  def :+(c: ClassName): ClassName = {
    list ++= c.list
    this
  }

  def :-(c: ClassName): ClassName = {
    list --= c.list
    this
  }

  def filter(f: (String) => Boolean): ClassName = ClassName(list.filter(f))
}

object ClassName {

  def apply(s: String*): ClassName = {
    ClassName(mutable.HashSet.empty[String] ++ s.filterNot(_ == "").view)
  }

  def empty: ClassName = {
    NilClass
  }

  implicit def string2ClassName(s: String): ClassName = {
    ClassName(s)
  }
}

object c {

  def apply(s: String*): ClassName = {
    ClassName(mutable.HashSet.empty[String] ++ s.filterNot(_ == "").view)
  }
}

object NilClass extends ClassName(mutable.HashSet.empty[String])

