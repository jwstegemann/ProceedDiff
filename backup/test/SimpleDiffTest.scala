package test

/**
  * Created by tiberius on 06.06.16.
  */

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
