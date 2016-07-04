package proceed.tree.html

import proceed.tree.Element

case class p(align: Option[String] = None,
                     title: Option[String] = None,
                     lang: Option[String] = None,
                     dir: Option[String] = None,
                     className: Option[String] = None) extends Element {

  override val fields = "align" ::
    "title" ::
    "lang" ::
    "dir" ::
    "className" :: Nil

  /*def onClick(handler: (Click.Event => Unit)): p = {
    on(Click)(handler)
    this
  }*/

}




