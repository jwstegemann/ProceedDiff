package proceed.tree

import scala.collection.mutable

/**
  * Classnames
  *
  * holds all specified classNames in a mutable.HashSet
  * and present some methods for adding or removing classNames
  *
  * @author Jan Weidenhaupt
  */
sealed case class ClassName(private final val list: mutable.HashSet[String]) {

  override def toString: String = list.mkString(" ")

  lazy val size: Int = list.size

  def add(c: ClassName): ClassName = {
    list ++= c.list
    this
  }

  def addif(c: ClassName)(f: => Boolean): ClassName = if(f) add(c) else this

  def remove(c: ClassName): ClassName = {
    list --= c.list
    this
  }

  def removeif(c: ClassName)(f: => Boolean): ClassName = if(f) remove(c) else this

  def filter(f: (String) => Boolean): ClassName = ClassName(list.filter(f))
}

object ClassName {

  def apply(s: String): ClassName = {
    if(!s.trim.isEmpty) ClassName(mutable.HashSet.empty[String] + s)
    else empty
  }

  def empty: ClassName = ClassName(mutable.HashSet.empty[String])

  implicit def string2ClassName(s: String): ClassName = ClassName(s)
}

