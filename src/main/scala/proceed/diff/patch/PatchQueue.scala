package proceed.diff.patch

import scala.collection.mutable

/**
  * Created by tiberius on 28.06.16.
  */
class PatchQueue extends mutable.Queue[Patch] {

  def execute() = foreach(_.apply)

}
