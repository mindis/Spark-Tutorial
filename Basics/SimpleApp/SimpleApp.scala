import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object SimpleApp {
	def main(args: Array[String]) {
		val logFile = "../README.md"
		val sc = new SparkContext("local", "Simple App", "~/toolkit/spark-0.9.1", List("target/scala-2.10/simple-project_2.10-1.0.jar"))
		val txt = sc.textFile(logFile, 2).cache()
		val numAs = txt.filter(line => line.contains("a")).count()
		val numBs = txt.filter(line => line.contains("b")).count()

		println("Line with a: %s\t, Line with b: %s\n".format(numAs, numBs))

	}
}
