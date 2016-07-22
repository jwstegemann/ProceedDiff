package proceed

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object DataBindingMacros {

  def bindImpl[T: c.WeakTypeTag, U: c.WeakTypeTag, E: c.WeakTypeTag]
    (c: blackbox.Context)
    (eventType: c.Expr[E])(path: c.Expr[T => U]): c.Expr[(E, Any)] = {

    import c.universe._

    val stateExpression = c.Expr[T](q"state")
    val modification = com.softwaremill.quicklens.QuicklensMacros.modify_impl(c)(stateExpression)(path)

    c.Expr[(E, Any)](
      q"""($eventType, (on: StatefullComponent[${symbolOf[T]}]) =>
         (e: Input.Event) => {
          on.setState($modification.setTo(e.input.value.toInt))
          println("IM MACROss!!" + $modification + "!!!" + state + "!!!!!!!!!!!!" + e)
         })""")
  }

}
