package proceed.util

import proceed.tree.{ChildMapImpl, Element, Node}

object PrettyPrinter {

  def apply(node: Node): String = {
    val builder = new StringBuilder
    handle(0, builder, node)
    builder.toString()
  }

  def handle(level: Int, builder: StringBuilder, node: Node): Unit = {
    node match {
      case n: Element => handle(level, builder, n)
//      case n: Component => handle(level, builder, n)
//      case n: TextNode => handle(level, builder, n)
    }
  }

  def printObjectAndId(level: Int, builder: StringBuilder, node: Node): Unit = {
    builder.append("  " * level)

    builder.append(node.toString)
    builder.append("#")
    builder.append(node.key.getOrElse("noName"))

    builder.append("\n")
  }

  def handle(level: Int, builder: StringBuilder, node: Element): Unit = {
    printObjectAndId(level, builder, node)
    node.children match {
      case (c: ChildMapImpl) => c.foreach { case (key: String, (p: Int, n: Node)) => handle(level+1, builder, n) }
      case _ =>
    }
  }

/*  def handle(level: Int, builder: StringBuilder, node: Component): Unit = {
    printObjectAndId(level, builder, node)
    handle(level + 1, builder, node.child)
  }

  def handle(level: Int, builder: StringBuilder, node: TextNode): Unit = {
    builder.append("  " * level)
    builder.append(node.content)
    builder.append("\n")
  }
*/
}
