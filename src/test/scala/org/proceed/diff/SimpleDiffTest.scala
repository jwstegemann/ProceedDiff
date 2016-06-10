package org.proceed.diff

/**
  * Created by tiberius on 06.06.16.
  */

import utest._

object SimpleDiffTest extends TestSuite {

  val tests = this {
    'test1 {
      throw new Exception("Hier geht etwas nicht")
    }
    'test2 {
      val a = List[Byte](1,2)
      a(10)
    }
  }

}
