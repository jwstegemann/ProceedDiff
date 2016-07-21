package proceed

import proceed.diff.RenderQueue
import proceed.diff.patch.PatchQueue
import proceed.util.log


object App {

  log.setThresholdFromUrl()

  val renderQueue: RenderQueue = new RenderQueue()

  /*
   * event-loop
  */
  var actualPatchQueue:Option[PatchQueue] = None

  def eventLoop(innerLoop: (PatchQueue) => Unit) = {
    //FIXME: ensure that this is never running parallel
    actualPatchQueue match {
      case None => {
        actualPatchQueue = Some(new PatchQueue())

        innerLoop(actualPatchQueue.get)

        log.debug(s"### returned from inner loop $renderQueue")

        while (renderQueue.nonEmpty) {
          renderQueue.renderNext()
        }

        actualPatchQueue.get.execute()
        actualPatchQueue = None
      }
      case Some(patchQueue) => innerLoop(patchQueue) //TODO: oder neu und in alt einfügen?
    }

  }

}
