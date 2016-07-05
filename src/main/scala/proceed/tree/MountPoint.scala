package proceed.tree

object MountPoint {
  def apply(id: String) = {
    val mp = new MountPoint()
    mp.id = id
    mp
  }
}

case class MountPoint() extends Element {
  override val fields: Seq[String] = Nil

  override def apply(c: Node, cs: Node*): Element = throw new UnsupportedOperationException
  override def apply(cs: Seq[Node]): Element with Product = throw new UnsupportedOperationException
  override def apply(): Element = throw new UnsupportedOperationException
}

