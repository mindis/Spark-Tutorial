abstract class Element {
	def contents: Array[String]
	def height: Int = contents.length
	def width: Int = if (height == 0) 0 else contents(0).length
}

/*
// code smell
class ArrayElement(conts: Array[String]) extends Element {
	def contents: Array[String] = conts
}
*/

// parametric field
class ArrayElement(val contents: Array[String]) extends Element

val ae = new ArrayElement(Array("hello", "world!"))

println(ae.height)
println(ae.width)


class LineElement(s: String) extends ArrayElement(Array(s)) {
	override def width = s.length
	override def height = 1
}

val le = new LineElement("hello, world")
println(le.height)
println(le.width)

println("*" * 20)

class UniformElement(ch: Char, 
		     override val width: Int,
		     override val height: Int
		    )extends Element {

	private val line = ch.toString * width
	def contents = Array.make(height, line)
}

