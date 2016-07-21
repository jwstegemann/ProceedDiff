package proceed.tree.html

import proceed.tree.{ClassName, Element}

case class button(accessKey: Option[String] = None,
                  disabled: Option[Boolean] = None,
                  name: Option[String] = None,
                  tabIndex: Option[Int] = None,
                  value: Option[String] = None,
                  title: Option[String] = None,
                  lang: Option[String] = None,
                  dir: Option[String] = None,
                  typeName: Option[String] = None,
                  className: ClassName = ClassName.empty) extends Element {


  override val fields =
    "accessKey" ::
    "disabled" ::
    "name" ::
    "tabIndex" ::
    "value" ::
    "typeName" ::
    "title" ::
    "lang" ::
    "dir" ::
    "className" :: Nil

}
