package proceed.tree.manual

import proceed.events.{Click, MouseEvent}
import proceed.tree.html._
import proceed.tree._

/**
  * Created by tiberius on 10.06.16.
  */
object MainNew {

  case class SimpleComponent(p1: String, p2: Int) extends StatefullComponent[MyState] {

    def increase(e: MouseEvent) = {
      setState(state.copy(to = state.to+1))
    }

    override def view(): Element = {
      div()(
        div(title=Some("p5")),
        p(title=Some("p3"))
          onClick(increase)
          as("HalloWelt"),
        if (state.from > 4) button(title=Some("p7")) else div() as "sonst",
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

    println("########################################")

    val mp = c1.parent.asInstanceOf[MountPoint]

    mp.handleEvent("mp.0.HalloWelt",Click)(MouseEvent(1,2,true,true,true,false))


  }

}
