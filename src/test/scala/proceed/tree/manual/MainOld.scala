package proceed.tree.manual

object MainOld {

  def dec(x: Int) = if (x>0) x-1 else x

  def main(args: Array[String]) {

    val alt = List("3","5","7","9","ende")

    val neu = List("5","9","7","11","13","ende")


    var a = alt.length-2
    var n = neu.length-2


    while (n > -1 || a > -1) {

      //      assert(a >= -1 && n >= -1)

      val e_alt = if(a >=0) alt(a) else ""
      val e_neu = if(n >=0) neu(n) else ""


      println("comparing neu(" + e_neu + "@" + n + ") with alt(" + e_alt + "@" + a + ")")

      if (e_alt == e_neu) {
        a = dec(a)
        n = dec(n)
      }
      else if (!alt.contains(e_neu)) {
        println("create " + e_neu + " before " + neu(n+1))
        n = dec(n)
      }
      else if (!neu.contains(e_alt)) {
        println("delete " + e_alt)
        a = dec(a)
      }
      else if (neu.indexOf(e_alt) <= n) {
        println("move " + e_neu + " before " + neu(n+1))
        n = dec(n)
      }
      else {
        a = dec(a)
      }

    }

  }

}