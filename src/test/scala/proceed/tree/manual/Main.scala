package proceed.tree.manual

import proceed.tree.{Element, Node}
import proceed.tree.html.{button, div, p}
import proceed.util.PrettyPrinter

/**
  * Created by tiberius on 10.06.16.
  */
object Main {

  def main(args: Array[String]) {

    /*val oldDom = div()(
      p(title=Some("p3")) aes "p3",
      p(title=Some("p4")) as "p4",
      p(title=Some("p7")) as "p7"
    )


    val newDom = div()(
      p(title=Some("p8")) as "p8",
      p(title=Some("p3")) as "p3",
      p(title=Some("p6")) as "p6",
      p(title=Some("p7")) as "p7"
    )
*/
    val oldDom = div()(
      p(title=Some("o3")),
      div(title=Some("o5")),
      button(title=Some("o7"))
    )

    val newDom = div()(
      div(title=Some("p5")),
      p(title=Some("p3")),
      button(title=Some("p7"))
    )

    println("Old DOM:")
    println(PrettyPrinter(oldDom))
    println("New DOM:")
    println(PrettyPrinter(newDom))

    val oldList = oldDom.children
    val newList = newDom.children


    val oldIterator = oldList.iterate
    val newIterator = newList.iterate


    while (!(newIterator.done && oldIterator.done)) {

      println("comparing old(" + oldIterator.currentKey + ") with new(" + newIterator.currentKey + ")")

      if (oldIterator.currentKey == newIterator.currentKey) {

        println("...reuse " + oldIterator.currentItem + " for " + newIterator.currentItem)

        (oldIterator.currentItem, newIterator.currentItem) match {
          case (oldElement:Element, newElement: Element) => "reuse Element"
          //TODO: Components
        }
        oldIterator.continue()
        newIterator.continue()
      }
      if (!newIterator.done && oldList.indexOf(newIterator.currentKey()).isEmpty) {
        newIterator.lastItem match {
          case Some(beforeItem: Node) => println("# create " + newIterator.currentItem + " before " + beforeItem)
          case None => println("# append " + newIterator.currentItem)
        }
        newIterator.continue()

        if (oldIterator.currentItem().key.isEmpty) {
          println("# delete " + oldIterator.currentItem)
          oldIterator.continue()
        }
      }
      //TODO: is checking für oldIterator here right?
      else if (!oldIterator.done) {
        newList.indexOf(oldIterator.currentKey()) match {
          case Some((pos: Int, node: Node)) => {
            if (pos <= newIterator.currentPos()) {
              newIterator.lastItem match {
                case Some(beforeItem: Node) => println("# move " + newIterator.currentItem + " before " + beforeItem)
                case None => println("# move " + newIterator.currentKey + " to end")
              }
              newIterator.continue()
            } else {
              oldIterator.continue()
            }
          }
          case None => {
            println("# delete " + oldIterator.currentItem)
            oldIterator.continue()
          }
        }
      }

    }

    // CASE

/*
    case class Test(a:String, b: Int, c:Option[Int])

    val fields: Seq[String] = "a" :: "b" :: "c" :: Nil

    val x = Test("a",17,None)
    val y = Test("b",18,Some(1))

/*    def getElements(product: Product) = for (
      index <- Range(0, fields.length);
      fieldName <- fields(index)
      ) yield (fieldName, product.productElement(index))

    def getElements(oldProduct: Product, newProduct: Product) = fields.iterator.zip(oldProduct.productIterator.zip(newProduct.productIterator))

    for (v <- getElements(x,y)) { println(v) }
*/
*/

  }

}
