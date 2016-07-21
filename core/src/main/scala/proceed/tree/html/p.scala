package proceed.tree.html

import proceed.tree.{ClassName, Element, NilClass}

case class p(align: Option[String] = None,
                     title: Option[String] = None,
                     lang: Option[String] = None,
                     dir: Option[String] = None,
                     className: ClassName = NilClass) extends Element {

  override val fields = "align" ::
    "title" ::
    "lang" ::
    "dir" ::
    "className" :: Nil

}




