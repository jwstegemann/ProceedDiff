package proceed.actions

import proceed.App
import proceed.diff.patch.PatchQueue

import scala.collection.mutable


trait Subscriber {
  private val publishers: mutable.HashSet[Store] = new mutable.HashSet[Store]()

  def subscribe(publisher: Store) = {
    publisher.subscribe(this)
    publishers.add(publisher)
  }

  def unsubscribe(publisher: Store) = {
    publisher.unsubscribe(this)
    publishers.remove(publisher)
  }

  def unsubscribeAll() = {
    publishers.foreach(_.unsubscribe(this))
  }

  def receive(patchQueue: PatchQueue)
}


trait Store {

  private val subscribers: mutable.HashSet[Subscriber] = new mutable.HashSet[Subscriber]()

  private[proceed] def subscribe(receiver: Subscriber) = subscribers.add(receiver)
  private[proceed] def unsubscribe(receiver: Subscriber) = subscribers.remove(receiver)

  def emit() = {
    App.eventLoop((pq) => {
      subscribers.foreach(_.receive(pq))
    })
  }
}


/*
 * Example
 */

/*
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
*/
