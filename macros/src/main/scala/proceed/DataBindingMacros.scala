package proceed

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object DataBindingMacros {

  def bindImpl[T: c.WeakTypeTag, U: c.WeakTypeTag, E: c.WeakTypeTag]
    (c: blackbox.Context)
    (eventType: c.Expr[E])(path: c.Expr[T => U]): c.Expr[(E, Any)] = {

    import c.universe._

    c.Expr[(E, Any)](
      q"""($eventType, (on: Component) =>
         (e: Input.Event) => {
          println("IM MACRO!!!!!!!!!!!!!!!!!" + e)
         })""")
  }

}
