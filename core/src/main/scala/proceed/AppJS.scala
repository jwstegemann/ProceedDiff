package proceed

import com.softwaremill.quicklens._
import proceed.events._
import proceed.store.Store
import proceed.tree.html._
import proceed.tree.{Component, Element, StatefullComponent}
import proceed.util.log

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

  def increase(e: MouseEvent) : Any = {
    RangeStore.inc()
  }

  def storeTo(e: TextEvent) : Any = {
    try {
      setState(state.modify(_.to).setTo(e.input.value.toInt))
      RangeStore.to = e.input.value.toInt
    } catch {
      case ex: NumberFormatException => log.error(s"the input text must be a number, not '${e.input.value}'");
    }

    //TODO: set(_.to, e.input.value.toInt)
  }

  override def view(): Element = {
    log.info(s"rendering SimpleComponent with state.from=${RangeStore.from} and state.to=${RangeStore.to}")

    div()(
      input(value = RangeStore.to.toString) ! onInput(_.storeTo),
      p()(
        s"Ihre Eingabe lautet: ${RangeStore.to}"
      ),
      button(className = "increase")(
        "increase"
      ) ! on(Click)(_.increase),
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

  def decrease(e: MouseEvent) = {
    RangeStore.dec()
  }

  override def view() = {
    log.info(s"rendering cMiddleComponent y with state.from=${state.from} and state.to=${state.to}")

    div()(
      if(RangeStore.to > 6) {
        button(className = "decrease")(
          "decrease"
        ) ! on(Click)(_.decrease)
      } else div(),
      MoreComplexComponent(from, to, true)
    )

  }
}

case class MoreComplexComponent(from: Int, to: Int, p3: Boolean) extends Component {
  override def view(): Element = {
    println(s"rendering MoreComplexComponent with from=$from and to=$to")

    div(className = "entries")(
      for (index <- Range(from, to)) yield p(className = s"entry$index")(s"Eintrag Nr. $index") as s"p$index"
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
