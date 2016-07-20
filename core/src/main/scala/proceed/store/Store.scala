package proceed.store

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

