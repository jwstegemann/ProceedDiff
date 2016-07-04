package proceed.tree.manual

import proceed.tree.html.{button, div, p}
import proceed.tree.manual.MainNew.MoreComplexComponent
import proceed.tree.{Component, Element, Node}

/**
  * Created by tiberius on 10.06.16.
  */
object MainNew {

  case class SimpleComponent(p1: String, p2: Int) extends Component {
    override def view(): Element = {
      div()(
        div(title=Some("p5")),
        p(title=Some("p3")) as("HalloWelt"),
        button(title=Some("p7")),
        MoreComplexComponent(1,2,true)
      )
    }
  }

  case class MoreComplexComponent(p1: Int, p2: Int, p3: Boolean) extends Component {
    override def view(): Element = {
      div()(
        for (index <- Range(0,3)) yield (p() as s"p$index")
      )
    }
  }


  def main(args: Array[String]) {

    val c1 = SimpleComponent("test",17)

    c1.mount("mp")

  }

}
