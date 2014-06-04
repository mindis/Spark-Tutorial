def isort(xs: List[Int]): List[Int] = 
	if (xs.isEmpty) Nil
	else insert(xs.head, isort(xs.tail))

def insert(x: Int, xs: List[Int]): List[Int] = 
	if (xs.isEmpty || x <= xs.head) x::xs
	else xs.head :: insert(x, xs.tail)

val nums = 2::1::7::3::1::Nil

val sorted = isort(nums)

for (n <- sorted)
	println(n)

println("*" * 20)

val a::b::c = sorted

println(a)
println(b)
println(c)

println(sorted::c)
println(sorted:::c)
println(sorted)
println(sorted.indices)

val zipped = sorted zip sorted.indices
println(zipped)

println(sorted.zipWithIndex)

println("-" * 20)

println(sorted.toString)

val mks = sorted.mkString("[", ";", "]")
println(mks)
println(sorted)

println(sorted map (_ + 10))
//println(sorted flatMap (_ + 10))
println("-" * 20)

val interplay = List.range(1,5) flatMap (
	i => List.range(1, i) map (j=>(i,j))
)
println(interplay)


println("-" * 20)
def pair() = for (i <- List.range(1, 5); j <- List.range(1, i))
	yield (i, j)

val p = pair()
println(p)

var sum = 0
sorted foreach(sum += _) 
println(sum)

val mod = sorted filter (_ % 2 == 0)

println(mod)

println("*" * 20)

val diag3 = List(1, 0, 0)::List(0, 1,0)::List(0,0,1)::Nil
println(diag3)

def hasZeroRow(m : List[List[Int]]) = 
	m exists (row => row forall (_ == 0))

val hz = hasZeroRow(diag3)
println(hz)


val words = "the"::"quick"::"brown"::"fox"::Nil
println(words)

println((words.head /: words.tail)(_ + " " + _))

def reverseLeft[T] (xs: List[T]) = 
	(List[T]() /: xs) {(ys, y) => y :: ys}

val rl = reverseLeft(sorted)
println(rl)
