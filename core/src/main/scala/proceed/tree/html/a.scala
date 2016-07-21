package proceed.tree.html

import proceed.tree.{ClassName, Element, NilClass}

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
             className: ClassName = NilClass) extends Element {

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