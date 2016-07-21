package proceed

import proceed.diff.RenderQueue
import proceed.diff.patch.PatchQueue
import proceed.util.log


object App {

  val renderQueue: RenderQueue = new RenderQueue()

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
