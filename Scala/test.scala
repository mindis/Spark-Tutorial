val filesHere = (new java.io.File(".")).listFiles


def fileLines(file: java.io.File) = 
	scala.io.Source.fromFile(file).getLines.toList


/*
def grep(pattern: String) = 
	for (
		file <- filesHere;
		if file.getName.endsWith(".scala");
		line <- fileLines(file);
		trimmed = line.trim;
		if trimmed.matches(pattern)
	) println(file + ": " + trimmed)

grep(".*gcd.*")

*/

val forLineLengths = 
	for (
		file <- filesHere
		if file.getName.endsWith(".scala");
		line <- fileLines(file);
		trimmed = line.trim
		if trimmed.matches(".*for.*")
	) yield trimmed.length

println(forLineLengths.length)


for (l <- forLineLengths)
	println(l)


val firstArg = if(!args.isEmpty) args(0) else ""
val friend = firstArg match {

	case "salt" => "pepper"
	case "chips" => "salsa"
	case "eggs" => "bacon"
	case _ => "huh?"
}

println(friend)

import java.io.PrintWriter
import java.io.File

def withPrintWriter(file: File)(op: PrintWriter => Unit) {
	val writer = new PrintWriter(file)
	try {
		op(writer)
	} finally {
		writer.close()
	}
}

val file = new File("date.txt")
withPrintWriter(file)(writer => writer.println(new java.util.Date))
