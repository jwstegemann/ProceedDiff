package proceed

import proceed.events.{Click, MouseEvent}
import proceed.tree.html._
import proceed.tree.{Component, Element, StatefullComponent}
import proceed.util.ClassName

import scala.scalajs.js.JSApp


case class SimpleComponent(p1: String, p2: Int) extends StatefullComponent[MyState] {

  def increase(e: MouseEvent) = {
    setState(state.copy(to = state.to+1))
  }

  override def view(): Element = {
    println(s"rendering SimpleComponent with state.from=${state.from} and state.to=${state.to}")

    div()(
      p()(
        "increase"
      ).on(Click, this)(_.increase(_)).as("dummy"),
      if (state.to > 4) button(title=Some("p7")) else div() as "sonst",
      MiddleComponent(state.from, state.to)
    )
  }

  override def initialState() = MyState(0,4)

}

case class MyState(from: Int, to: Int)

case class MiddleComponent(from: Int, to: Int) extends StatefullComponent[MyState] {

  override def initialState() = MyState(from, to)


  override def parametersChanged() = {
    setState(initialState())
  }

  def decrease(e: MouseEvent)(x: Int) = {
    setState(state.copy(to = state.to-1))
  }

  override def view() = {
    println(s"rendering MiddleComponent with state.from=${state.from} and state.to=${state.to}")

    div(className = ClassName(if(state.to > 4) "Test" else "") + (if(state.from < 10) "Test1" else ""))(
      p()(
        "decrease"
      ).on(Click, this)(_.decrease(_)(17)).as("dummy"),
      //        p().on(Click, this)((c: MiddleComponent, e: MouseEvent) => setState(state.copy(to = state.to-1))).as("dummy"),
      MoreComplexComponent(state.from, state.to, true)
    )

  }

}

case class MoreComplexComponent(from: Int, to: Int, p3: Boolean) extends Component {
  override def view(): Element = {
    println(s"rendering MoreComplexComponent with from=$from and to=$to")

    div(title = "Test")(
      for (index <- Range(from, to)) yield (p()(s"Eintrag Nr. $index") as s"p$index")
    )
  }
}

object AppJS extends JSApp {

  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    val c = SimpleComponent("test",17)
    c.mount("mp")
  }

}
