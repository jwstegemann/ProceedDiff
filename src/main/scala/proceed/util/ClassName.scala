package proceed.util


import scala.collection.mutable

/**
  * Classnames
  *
  * @author Jan Weidenhaupt
  *         13.07.2016
  */
case class ClassName(classes: mutable.Set[String]) {
  override def toString(): String = {
    classes.mkString(" ")
  }
}

object ClassName {
  def apply(s: String*): ClassName = {
    ClassName(mutable.Set.empty ++ s.filterNot(_ == "").view)
  }
}

object NilClass extends ClassName(mutable.Set.empty)

