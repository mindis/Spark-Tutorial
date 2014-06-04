// http://twitter.github.io/scala_school/collections.html

def timesTwo(i: Int) = i * 2

val numbers = 1::2::3::4::5::6::7::8::9::10::Nil
println(numbers.map((i: Int) => i * 2))
println(numbers.map(timesTwo _))

numbers.foreach(println)

println(numbers.filter((i:Int)=> i % 2 == 0))

println(numbers.zip(List('a', 'b', 'c')))

println(numbers.zipWithIndex)

println(numbers.find(_ > 3))
println(numbers.find((i: Int) => i > 2))

println(numbers.foldLeft(100)((m: Int, n: Int) => m + n))
numbers.foldLeft(0) { (m: Int, n: Int) => println("m: " + m + " n: " + n); m + n }

val nestedNumbers = List(List(1, 2), List(3, 4))


println(nestedNumbers.flatMap(x => x.map(_ * 2)))
// Think of it as short-hand for mapping and then flattening:
println(nestedNumbers.map((x:List[Int]) => x.map(_ * 2)).flatten)


println("-" * 20)
println(numbers)

def ourMap(numbers: List[Int], fn: Int => Int): List[Int] = {

	numbers.foldRight(List[Int]()) {(x: Int, xs:List[Int]) => 
		fn(x)::xs
	}
}

println(ourMap(numbers, timesTwo(_)))
