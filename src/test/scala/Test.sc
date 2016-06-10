

val alt = List("a","b","c","d")

val neu = List("a","b","c","d")


var a = alt.length-1
var n = neu.length-1


while (a >= 0 || n >= 0) {
  
  val e_alt = if(a >=0) alt(a) else ""
  val e_neu = if(n >=0) neu(n) else ""
  
  
  println("comparing neu(" + e_neu + "@" + n + ") with alt(" + e_alt + "@" + a + ")")

  if (e_alt == e_neu) {
    a -= 1
    n -= 1
  }
  else if (!alt.contains(e_neu)) {
    println("create " + e_neu + "@" + n)
    n -= 1
  }
  else if (!neu.contains(e_alt)) {
    println("delete " + e_alt)
    a -= 1
  }
  else if (neu.indexOf(e_alt) <= n) {
    println("move " + e_alt + "@" + (n-1))
    n -= 1
  }
  else {
    a -= 1
  }

}
