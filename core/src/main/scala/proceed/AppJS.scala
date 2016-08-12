package proceed

import proceed.style._
import proceed.tree._
import proceed.tree.html._

import scala.scalajs.js.JSApp
import scalacss.Defaults._


object MyStyle extends Style {
  import dsl._

  val common = mixin(
    backgroundColor.green
  )

  val outer = style(
    common, // Applying our mixin
    margin(12 px, auto),
    textAlign.left,
    cursor.pointer,

    &.hover(
      cursor.zoomIn
    ),

    media.not.handheld.landscape.maxWidth(640 px)(
      width(400 px)
    )
  )

  /** Style requiring an Int when applied. */
  val indent =
  styleF.int(0 to 3)(i => styleS(
    paddingLeft(i * 2.ex)
  ))

  /** Style hooking into Bootstrap. */
  val button = style(
    addClassNames("btn", "btn-default")
  )
}


case class SimpleComponent() extends Component {

  override def view(): DomNode = {
    div()(
      button(className = MyStyle.indent(1) + MyStyle.outer, style = "height: 30px"),
      button(className = MyStyle.indent(3) + MyStyle.button)
    )
  }
}

object AppJS extends JSApp {

  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    App.style(MyStyle)
    val c = SimpleComponent()
    c.mount("mp")
  }

}
