val ownerPath = "mp.SimpleComponent:0.HalloWelt"

val ownerId = "ComplexComponent"

val x = "mp.SimpleComponent:0.HalloWelt.ComplexComponent:div.p"

val registerTarget = x.substring(ownerPath.length + ownerId.length+2)

val (path, target) = x.splitAt(x.lastIndexOf(':'))

println(path)
println(target)


