// http://twitter.github.io/scala_school/zh_cn/basics.html

def capitalizeAll(args: String*) = {
	args.map{arg => arg.capitalize}
}

println(capitalizeAll("chen", "wenqiang"))

class Calculator(brand: String) {

	// constructor
	val color: String = if (brand == "TI") {
		"blue"
	}else if (brand == "HR") {
		"black"
	}else {
		"white"
	}

	// instance method
	def add(m: Int, n: Int) = m + n
}

case class Calculator(brand: String, model: String)
