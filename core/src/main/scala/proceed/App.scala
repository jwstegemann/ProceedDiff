package proceed

import org.scalajs.dom.window.document
import proceed.diff.RenderQueue
import proceed.diff.patch.PatchQueue
import proceed.style.Style
import proceed.util.log


object App {

  val isDebug: Boolean = log.setThresholdFromUrl()

  val renderQueue: RenderQueue = new RenderQueue()


  def style(s: Style): Unit = {
    import scalacss.ProdDefaults._

    val styleElem = document.createElement("style")
    styleElem.innerHTML = s.render
    document.head.appendChild(styleElem)
  }

  /*
   * event-loop
  */
  var actualPatchQueue:Option[PatchQueue] = None

  def startEventLoop(innerLoop: (PatchQueue) => Unit) = {
    synchronized {

      actualPatchQueue = Some(new PatchQueue())

      eventLoop(innerLoop)

      while (renderQueue.nonEmpty) {
        renderQueue.renderNext()
      }

      actualPatchQueue.get.execute()
      actualPatchQueue = None
    }
  }

  def eventLoop(innerLoop: (PatchQueue) => Unit) = {
    actualPatchQueue match {
      case None => {
        log.fatal(s"trying to handle event without actual PatchQueue")
      }
      case Some(patchQueue) => {
        val partialPatchQueue = new PatchQueue()
        innerLoop(partialPatchQueue)
        actualPatchQueue.get.enqueue(partialPatchQueue)
      }
    }
  }

}
