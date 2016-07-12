package proceed.tree.manual

import proceed.diff.Diff
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
      println(s"rendering SimpleComponent with state.from=${state.from} and state.to=${state.to}")

      div()(
        p()(
          "Dies ist ein Test"
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

      div()(
        p().on(Click, this)(_.decrease(_)(17)).as("dummy"),
//        p().on(Click, this)((c: MiddleComponent, e: MouseEvent) => setState(state.copy(to = state.to-1))).as("dummy"),
        MoreComplexComponent(state.from, state.to, true)
      )

    }

  }

  case class MoreComplexComponent(from: Int, to: Int, p3: Boolean) extends Component {
    override def view(): Element = {
      println(s"rendering MoreComplexComponent with from=$from and to=$to")

      div()(
        for (index <- Range(from, to)) yield (p() as s"p$index")
      )
    }
  }


  def main(args: Array[String]) {

    val c1 = SimpleComponent("test",17)
    c1.mount("mp")

    println("########################################")

    val mp = c1.parent.asInstanceOf[MountPoint]

    mp.eventLoop((rq,pq) =>
      mp.handleEvent("SimpleComponent0" ::"0"::"dummy"::Nil,null,Click)(MouseEvent(1,2,true,true,true,false), rq, pq)
    )
    
    println("########################################")

    mp.eventLoop((rq,pq) =>
      mp.handleEvent("SimpleComponent0" :: "0" :: "MiddleComponent2" ::"0"::"dummy"::Nil,null,Click)(MouseEvent(1,2,true,true,true,false), rq, pq)
    )

  }

}
