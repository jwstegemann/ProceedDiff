package proceed.tree.html

import proceed.tree.Element

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
                 className: Option[String] = None) extends Element {


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

