package proceed.tree.html

import org.scalajs.dom.raw
import proceed.tree.Node

case class TextNode(content: String) extends Node {

  val fields: Seq[String] = "content" :: Nil

  var domRef: Option[raw.Text] = None

  def node: TextNode = this
}
