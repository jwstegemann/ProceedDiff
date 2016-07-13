package proceed.tree.html

import proceed.tree.Element
import proceed.util.{ClassName, NilClass}

case class div(align: Option[String] = None,
               title: Option[String] = None,
               lang: Option[String] = None,
               dir: Option[String] = None,
               className: ClassName = NilClass) extends Element {

  override val fields =
    "align" ::
    "title" ::
    "lang" ::
    "dir" ::
    "className" :: Nil
}
