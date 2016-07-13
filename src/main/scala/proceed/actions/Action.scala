package proceed.actions

import proceed.util.log

import scala.collection.mutable

trait Receiver {
  def dispatch: PartialFunction[Product, Unit]

  def receive(msg: Product) = {
    dispatch(msg)
  }

}

trait LoggingReceiver extends Receiver {
  override def receive(msg: Product) = {
    log.debug(s"receiver $this received message '$msg'")
    super.receive(msg)
  }
}


trait Store extends Receiver with Publisher{

}

trait Subscriber extends Receiver {
  private val publishers: mutable.HashSet[Publisher] = new mutable.HashSet[Publisher]()

  def subscribe(publisher: Publisher) = {
    publisher.subscribe(this)
    publishers.add(publisher)
  }

  def unsubscribe(publisher: Publisher) = {
    publisher.unsubscribe(this)
    publishers.remove(publisher)
  }

  def unsubscribeAll() = {
    publishers.foreach(_.unsubscribe(this))
  }
}


trait Publisher {

  private val subscribers: mutable.HashSet[Receiver] = new mutable.HashSet[Receiver]()

  def subscribe(receiver: Receiver) = subscribers.add(receiver)
  def unsubscribe(receiver: Receiver) = subscribers.remove(receiver)

  def emit(msg: Product) = {
    subscribers.foreach(_.receive(msg))
  }
}

case class ReRender()


/*
 * Example
 */

case class Message1(p1: String)
case class Message2(p2: Int)


object MyStore extends Store with Publisher with LoggingReceiver {

  override def dispatch = {
    case msg: Message1 => println("received Message1")
    case msg: Message2 => println("received Message2")
  }

  def main() = {
    this.receive(Message1("Hallo Welt2222"))
  }
}