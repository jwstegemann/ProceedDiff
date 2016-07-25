package proceed.tree.html

import org.scalajs.dom.raw
import proceed.tree.{ClassName, Element, Node}

/**
  * All basic html-elements
  *
  * @see https://developer.mozilla.org/en-US/docs/Web/HTML/Element
  * @author Jan Weidenhaupt
  */

case class TextNode(content: String) extends Node {

  val fields: Seq[String] = "content" :: Nil

  var domRef: Option[raw.Text] = None

  def node: TextNode = this
}


case class a(accessKey: Option[String] = None,
             charset: Option[String] = None,
             coords: Option[String] = None,
             href: Option[String] = None,
             hreflang: Option[String] = None,
             name: Option[String] = None,
             rel: Option[String] = None,
             rev: Option[String] = None,
             shape: Option[String] = None,
             tabIndex: Option[String] = None,
             target: Option[String] = None,
             typeName: Option[String] = None,
             title: Option[String] = None,
             lang: Option[String] = None,
             dir: Option[String] = None,
             className: ClassName = "") extends Element {

  override val fields =
    "accessKey" ::
      "charset" ::
      "coords" ::
      "href" ::
      "hreflang" ::
      "name" ::
      "rel" ::
      "rev" ::
      "shape" ::
      "tabIndex" ::
      "target" ::
      "typeName" ::
      "title" ::
      "lang" ::
      "dir" ::
      "className" :: Nil

}

case class button(accessKey: Option[String] = None,
                  disabled: Option[Boolean] = None,
                  name: Option[String] = None,
                  tabIndex: Option[Int] = None,
                  value: Option[String] = None,
                  title: Option[String] = None,
                  lang: Option[String] = None,
                  dir: Option[String] = None,
                  typeName: Option[String] = None,
                  className: ClassName = "") extends Element {


  override val fields =
    "accessKey" ::
      "disabled" ::
      "name" ::
      "tabIndex" ::
      "value" ::
      "title" ::
      "lang" ::
      "dir" ::
      "typeName" ::
      "className" :: Nil

}

case class div(align: Option[String] = None,
               title: Option[String] = None,
               lang: Option[String] = None,
               dir: Option[String] = None,
               className: ClassName = "") extends Element {

  override val fields =
    "align" ::
      "title" ::
      "lang" ::
      "dir" ::
      "className" :: Nil
}

case class input(defaultValue: Option[String] = None,
                 defaultChecked: Option[Boolean] = None,
                 accept: Option[String] = None,
                 accessKey: Option[String] = None,
                 align: Option[String] = None,
                 alt: Option[String] = None,
                 checked: Option[Boolean] = None,
                 disabled: Option[Boolean] = None,
                 maxLength: Option[Int] = None,
                 name: Option[String] = None,
                 readOnly: Option[Boolean] = None,
                 size: Option[Int] = None,
                 src: Option[String] = None,
                 tabIndex: Option[Int] = None,
                 useMap: Option[String] = None,
                 value: Option[String] = None,
                 typeName: Option[String] = None,
                 title: Option[String] = None,
                 lang: Option[String] = None,
                 dir: Option[String] = None,
                 className: ClassName = "") extends Element {

  override val fields =
    "defaultValue" ::
      "defaultChecked" ::
      "accept" ::
      "accessKey" ::
      "align" ::
      "alt" ::
      "checked" ::
      "disabled" ::
      "maxLength" ::
      "name" ::
      "readOnly" ::
      "size" ::
      "src" ::
      "tabIndex" ::
      "useMap" ::
      "value" ::
      "typeName" ::
      "title" ::
      "lang" ::
      "dir" ::
      "className" :: Nil
}

case class p(align: Option[String] = None,
             title: Option[String] = None,
             lang: Option[String] = None,
             dir: Option[String] = None,
             className: ClassName = "") extends Element {

  override val fields = "align" ::
    "title" ::
    "lang" ::
    "dir" ::
    "className" :: Nil

}