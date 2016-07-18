package proceed.util


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
final case class ClassName(list: mutable.HashSet[String]) {

  override def toString: String = list.mkString(" ")

  lazy val size: Int = list.size

  def +(s: String): ClassName = {
    list += s
    this
  }

  def ++(s: String*): ClassName = {
    list ++= s
    this
  }

  def -(s: String): ClassName = {
    list -= s
    this
  }

  def --(s: String*): ClassName = {
    list --= s
    this
  }

  def filter(f: (String) => Boolean): ClassName = ClassName(list.filter(f))
}

object ClassName {

  def apply(s: String*): ClassName = {
    ClassName(mutable.HashSet.empty[String] ++ s.filterNot(_ == "").view)
  }

  def empty: ClassName = {
    ClassName(mutable.HashSet.empty[String])
  }

  implicit def string2ClassName(s: String): ClassName = {
    ClassName(s)
  }
}

