package proceed.tree

/**
  * Created by tiberius on 14.06.16.
  */
package object html {

    implicit def string2Node(s: String): TextNode = TextNode(s)
    implicit def string2Option(s: String): Option[String] = Some(s)
    implicit def boolean2Option(b: Boolean): Option[Boolean] = Some(b)
    implicit def int2Option(i: Int): Option[Int] = Some(i)
}
