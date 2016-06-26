import org.proceed.tree.{div, p}

val x = new div()

var t: Int = x.xtype

println(t)

val y = new p()

println(y.xtype)

val z = new p()

println(z.xtype)
