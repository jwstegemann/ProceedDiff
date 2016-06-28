package proceed.diff.patch

import proceed.tree.Element

/**
  * Created by tiberius on 08.06.16.
  */
sealed trait Patch {
  def apply()
}

case class AppendNewChild(parent: Element, child: Element) extends Patch {
  def apply() = {

  }
}

case class InsertNewChild(parent: Element, child: Element, sibbling: Element) extends Patch {
  def apply() = {

  }
}

case class DeleteChild(parent: Element, child: Element) extends Patch {
  def apply() = {

  }
}

case class RemoveAttribute(element: Element, attribute: String) extends Patch {
  def apply() = {

  }
}

case class SetAttribute(element: Element, attribute: String, value: String) extends Patch {
  def apply() = {

  }
}

