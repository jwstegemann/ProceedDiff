package proceed.tree.html

import proceed.events.Click
import proceed.tree.{Component, Element}

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

/*  def onClick[E <: Component](handler: (E, Click.Event => Unit))(implicit owner: E): p = {
    on(Click)(handler)
    this
  }*/

}




