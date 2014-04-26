import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object wordCount {
	def main(args: Array[String]) {
		
		val data = "../README.md"
		val sc = new SparkContext("local", "Word Count", "~/toolkit/spark-0.9.1", Seq("target/scala-2.10/word-count_2.10-1.0.jar"))
		val txt = sc.textFile(data, 2).cache()

		val wordcount = txt.flatMap(line => line.split("\\s+")).map(word => (word, 1)).reduceByKey(_ + _)
		wordcount.saveAsTextFile("wordcount")

	}
}
