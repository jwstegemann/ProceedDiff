package proceed

import proceed.diff.RenderQueue
import proceed.diff.patch.PatchQueue
import proceed.util.log


object App {

  val renderQueue: RenderQueue = new RenderQueue()

  /*
   * event-loop
  */

  def eventLoop(innerLoop: (PatchQueue) => Unit) = {
    //FIXME: ensure that this is never running parallel
    val patchQueue = new PatchQueue()

    innerLoop(patchQueue)

    log.debug(s"### returned from inner loop $renderQueue")

    while (renderQueue.nonEmpty) {
      renderQueue.renderNext()
    }

    patchQueue.execute()
  }

}
