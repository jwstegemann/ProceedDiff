package proceed

import proceed.store.Store
import proceed.events._
import proceed.tree.html._
import proceed.tree.{Component, Element, StatefullComponent}
import com.softwaremill.quicklens._

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

  def to[E <: EventType](t: E)(m: (this.type, t.Event) => Any): (this.type, t.Event) => Any = m

  def increase(e: MouseEvent) : Any = {
    RangeStore.inc()
  }

  def storeTo(e: TextEvent) : Any = {
    setState(state.modify(_.to).setTo(e.input.value.toInt))
    RangeStore.to = e.input.value.toInt
    //TODO: set(_.to, e.input.value.toInt)
  }

  override def view(): Element = {
    println(s"rendering SimpleComponent with state.from=${state.from} and state.to=${state.to}")

    div()(
      input(defaultValue = RangeStore.to.toString) ! on(Input)(_.storeTo),
      p()(
        s"Ihre Eingabe lautet: ${state.to}"
      ),
      p()(
        "increase"
      ).as("dummy") ! on(Click)(_.increase),
      if (state.to > 4) button(title=Some("p7")) else div() as "xsonst",
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

  def decrease(x: Int)(e: MouseEvent) = {
    RangeStore.dec()
  }

  override def view() = {
    println(s"rendering cMiddleComponent y with state.from=${state.from} and state.to=${state.to}")

    div()(
      p()(
        "decrease"
      ).as("dummy") ! on(Click)(_.decrease(17)),
      //        p().on(Click, this)((c: MiddleComponent, e: MouseEvent) => setState(state.copy(to = state.to-1))).as("dummy"),
      MoreComplexComponent(from, to, true)
    )

  }
}

case class MoreComplexComponent(from: Int, to: Int, p3: Boolean) extends Component {
  override def view(): Element = {
    println(s"rendering MoreComplexComponent with from=$from and to=$to")

    div()(
      for (index <- Range(from, to)) yield p()(s"Eintrag Nr. $index") as s"p$index"
    )
  }
}

object AppJS extends JSApp {

  Macros.hello

  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    val c = SimpleComponent("test",17)
    c.mount("mp")
  }

}
