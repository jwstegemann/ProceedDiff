package proceed.tree.manual

import scala.collection.mutable
import proceed.tree.{ChildMap, Node}
import proceed.tree.html.{div, p}

/**
  * Created by tiberius on 10.06.16.
  */
object Main {

  def main(args: Array[String]) {

    val altDom = div()(
      p(title=Some("p3")) as "p3",
      p(title=Some("p5")) as "p5",
      p(title=Some("p7")) as "p7",
      p(title=Some("p9")) as "p9"
    )

    val newDom = div()(
      p(title=Some("p3")) as "p3",
      p(title=Some("p5")) as "p5",
      p(title=Some("p7")) as "p7"
    )

    val oldList = altDom.children
    val newList = newDom.children


    val oldIterator = oldList.iterate
    val newIterator = newList.iterate


    while (!(newIterator.done && oldIterator.done)) {

      println("comparing old(" + oldIterator.current + ") with new(" + newIterator.current + ")")

      //TODO: Algorithmus nur, wenn das neue Element einen Namen hat!!!!

      if (oldIterator.current._2.name == newIterator.current._2.name) {
        oldIterator.continue()
        newIterator.continue()
      }
      else if (oldList.indexOf(newIterator.currentName().get).isEmpty) {
        newIterator.lastChild match {
          case Some(lastElement: (String,(Int,Node))) => println("create " + newIterator.currentName() + " before " + lastElement._2._2.name)
          case None => println("append " + newIterator.currentName())
        }
        newIterator.continue()
      }
      else {
        newList.indexOf(oldIterator.currentName().get) match {
          case Some((pos: Int, node: Node)) => {
            if (pos <= newIterator.currentPos()) {
              newIterator.lastChild match {
                case Some(lastElement: (String,(Int,Node))) => println("move " + newIterator.currentName() + " before " + lastElement._2._2.name)
                case None => println("move " + newIterator.currentName() + " to end")
              }
              newIterator.continue()
            } else {
              oldIterator.continue()
            }
          }
          case None => {
            println("delete " + oldIterator.currentName())
            oldIterator.continue()
          }
        }
      }

    }
/*
//      assert(a >= -1 && n >= -1)

      val e_alt = if(a >=0) alt(a) else ""
      val e_neu = if(n >=0) neu(n) else ""


      println("comparing neu(" + e_neu + "@" + n + ") with alt(" + e_alt + "@" + a + ")")

      if (e_alt == e_neu) {
        a = dec(a)
        n -= 1
      }
      else if (!alt.contains(e_neu)) {
        println("create " + e_neu + " before " + neu(n+1))
        n -= 1
      }
      else if (!neu.contains(e_alt)) {
        println("delete " + e_alt)
        a = dec(a)
      }
      else if (neu.indexOf(e_alt) <= n) {
        println("move " + e_neu + " before " + neu(n+1))
        n -= 1
      }
      else {
        a = dec(a)
      }

    }
*/

  }

}
