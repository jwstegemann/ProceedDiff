package proceed.tree.html

import proceed.tree.{Element}

case class div(align: Option[String] = None,
               title: Option[String] = None,
               lang: Option[String] = None,
               dir: Option[String] = None,
               className: Option[String] = None) extends Element {

  override val fields =
    "align" ::
    "title" ::
    "lang" ::
    "dir" ::
    "className" :: Nil
}
