package proceed

import proceed.actions.Store
import proceed.events.{Click, MouseEvent}
import proceed.tree.html._
import proceed.tree.{Component, Element, StatefullComponent}
import proceed.util.{ClassName, log}

import scala.scalajs.js.JSApp


object RangeStore extends Store {

  var from: Int = 0
  var to: Int = 4

  def inc() = {
    to += 1
    emit()
  }

  def dec() = {
    to -= 1
    emit()
  }

}




case class SimpleComponent(p1: String, p2: Int) extends StatefullComponent[MyState] {

  def increase(e: MouseEvent) = {
    RangeStore.inc()
    //this.traverseComponents(c => println("found " + c))
  }

  override def view(): Element = {
    log.info(s"rendering SimpleComponent with state.from=${state.from} and state.to=${state.to}")

    div()(
      p()(
        "increase"
      ).on(Click, this)(_.increase(_)).as("dummy"),
      if (state.to > 4) button(title=Some("p7")) else div() as "sonst",
      MiddleComponent(RangeStore.from, RangeStore.to)
    )
  }

  override def init() = {
    subscribe(RangeStore)
  }

  override def initialState() = MyState(0,4)

}

case class MyState(from: Int, to: Int)

case class MiddleComponent(from: Int, to: Int) extends StatefullComponent[MyState] {

  override def initialState() = MyState(from, to)

  def decrease(e: MouseEvent)(x: Int) = {
    RangeStore.dec()
  }

  override def view() = {
    log.info(s"rendering MiddleComponent with state.from=${state.from} and state.to=${state.to}")

    div(className = (if(state.to > 4) "Test" else "") + (if(state.from < 10) "Test1" else ""))(
      p()(
        "decrease"
      ).on(Click, this)(_.decrease(_)(17)).as("dummy"),
      //        p().on(Click, this)((c: MiddleComponent, e: MouseEvent) => setState(state.copy(to = state.to-1))).as("dummy"),
      MoreComplexComponent(from, to, true)
    )

  }
}

case class MoreComplexComponent(from: Int, to: Int, p3: Boolean) extends Component {
  override def view(): Element = {
    log.info(s"rendering MoreComplexComponent with from=$from and to=$to")

    div(title = "Test")(
      for (index <- Range(from, to)) yield p()(s"Eintrag Nr. $index") as s"p$index"
    )
  }
}

object AppJS extends JSApp {

  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    //TODO: move this call to App.init
    log.setThresholdFromUrl()
    val c = SimpleComponent("test",17)
    c.mount("mp")
  }

}
