package proceed.tree.manual

import proceed.diff.patch.PatchQueue
import proceed.tree.html.{button, div, p}
import proceed.tree.manual.MainNew.MoreComplexComponent
import proceed.tree.{Component, Element, Node, StatefullComponent}

/**
  * Created by tiberius on 10.06.16.
  */
object MainNew {

  case class SimpleComponent(p1: String, p2: Int) extends StatefullComponent[MyState] {
    override def view(): Element = {
      div()(
        div(title=Some("p5")),
        p(title=Some("p3")) as("HalloWelt"),
        button(title=Some("p7")),
        MoreComplexComponent(state.from, state.to ,true)
      )
    }

    override def initialState() = MyState(0,4)

  }

  case class MyState(from: Int, to: Int)

  case class MoreComplexComponent(from: Int, to: Int, p3: Boolean) extends Component {

    override def view(): Element = {
      div()(
        for (index <- Range(from, to)) yield (p() as s"p$index")
      )
    }
  }


  def main(args: Array[String]) {

    val c1 = SimpleComponent("test",17)
    c1.mount("mp")

    println("########################################")

    c1.setState(MyState(5,7))

    c1.mount("mp")



  }

}
