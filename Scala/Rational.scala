class Rational(n: Int, d: Int) {
	require(d != 0)

	private val g = gcd(n.abs, d.abs)
	val numer: Int = n/g
	val denom: Int = d/g

	def this(n: Int) = this(n, 1)
	def this(n: Int, d: Int, c:Int) = this(n)
	override def toString = {
		if (denom != 1)
			numer + "/" + denom
		else
			numer + ""
	}

	def add(that: Rational): Rational = new Rational(numer * that.denom + that.numer * denom, denom * that.denom)

	def lessThan(that: Rational): Boolean = this.numer * that.denom < that.numer * this.denom

	def max(that: Rational): Rational = { 
		println("max of two rationals")
		if (lessThan(that)) that else this
	}

	def +(that: Rational) = add(that)
	def +(i: Int): Rational = new Rational(this.numer + i * denom, denom)

	def -(that: Rational): Rational = new Rational(this.numer * that.denom - that.numer * this.denom, denom * that.denom)
	def -(i: Int): Rational = new Rational(numer - i * denom, denom)
	def <(that: Rational) = lessThan(that)

	def *(that: Rational) = new Rational(numer * that.numer, denom * that.denom)
	def *(i: Int): Rational = new Rational(numer * i, denom)

	def /(that: Rational): Rational = new Rational(numer * that.denom, denom * that.numer)
	def /(i: Int): Rational = new Rational(numer, denom * i)

	// works only in the interpreter
	// implicit def intToRational(x: Int) = new Rational(x)
	private def gcd(a: Int, b:Int):Int = if (b == 0) a else gcd(b, a % b)
}

val a = new Rational(42,66)
val b = new Rational(2,7)

val c = a / b

println(c)

println(c * 2)
println(c / 2)
//println(2 * c)
