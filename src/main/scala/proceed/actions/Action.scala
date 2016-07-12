package proceed.actions

abstract class Subscriber {

  val dispatch: PartialFunction[Product, Unit]

}



case class ToDo(title: String, description: ToDo)

case class SaveToDoMsg(toDo: ToDo)


class TestComponent extends Subscriber {

  override val dispatch = {
    case msg: SaveToDoMsg => println("SaveToDo: " + msg)
  }

}

