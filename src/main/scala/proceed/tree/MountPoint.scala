package proceed.tree

import org.scalajs.dom
import proceed.diff.RenderQueue
import proceed.diff.patch.PatchQueue
import proceed.events._

object MountPoint {
  def apply(id: String) = {
    val mp = new MountPoint()
    mp.id = id
    mp.setEventListener()
    mp
  }
}

case class MountPoint() extends Element {
  override val fields: Seq[String] = Nil

  override def apply(c: Node, cs: Node*): Element = throw new UnsupportedOperationException
  override def apply(cs: Seq[Node]): Element with Product = throw new UnsupportedOperationException
  override def apply(): Element = throw new UnsupportedOperationException

  //TODO: error-handling
  //domRef = Some(dom.document.getElementById(id))

  def setEventListener() = {
  /*  //TODO: add only needed event handlers
    domRef.get.addEventListener("click", {
      (e: dom.Event) => handleNativeEvent(e)
    }, true)
    //  domRef.get.addEventListener("keypress", {
    //    (e: dom.Event) => handleNativeEvent(e)
    //  }, true)
    //  domRef.get.addEventListener("change", {
    //    (e: dom.Event) => handleNativeEvent(e)
    //  }, true)
    domRef.get.addEventListener("input", {
      (e: dom.Event) => handleNativeEvent(e)
    }, true)
  */}


  /*
   * event-loop
   */

  def eventLoop(innerLoop: (RenderQueue, PatchQueue) => Unit) = {
    val renderQueue = new RenderQueue()
    val patchQueue = new PatchQueue()

    innerLoop(renderQueue, patchQueue)

    while (!renderQueue.isEmpty) {
      val component = renderQueue.dequeue()
      component.render(patchQueue, component.parent, None)
    }

    patchQueue.execute()
  }


  def handleNativeEvent(rawEvent: dom.Event) = {
    rawEvent.stopPropagation()
    rawEvent.preventDefault()

    val source = rawEvent.srcElement.getAttribute("id")
    val (pathString, target) = source.splitAt(source.lastIndexOf(':'))
    val path = pathString.split(Array('.',':')).tail

    eventLoop((renderQueue: RenderQueue, patchQueue: PatchQueue) =>
      //FIXME: change order
      (rawEvent, rawEvent.`type`) match {
        case (e: dom.MouseEvent, "click") =>
          handleEvent(path, target, Click)(
            MouseEvent(e.clientX.toInt, e.clientY.toInt, e.altKey, e.ctrlKey, e.metaKey, e.shiftKey), renderQueue)
        case (e: dom.KeyboardEvent, "keypress") =>
          handleEvent(path, target, Keypress)(
            KeyboardEvent(e.keyCode, e.altKey, e.ctrlKey, e.metaKey, e.shiftKey), renderQueue)
        case (e: dom.Event, "change") =>
          handleEvent(path, target, Change)(
            TargetEvent(e.srcElement), renderQueue)
        case (e: dom.Event, "input") =>
          handleEvent(path, target, Input)(
            TargetEvent(e.srcElement), renderQueue)
      }
    )
    
  }
}

