import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object topK {
	def main(args: Array[String]) {
		val sc = new SparkContext("local", "Spark Join", "~/toolkit/spark-0.9.1", Seq("target/scala-2.10/spark-join_2.10-1.0.jar"))	
		// read ratings
		val data = "ml-1m/ratings.dat"
		val txt = sc.textFile(data)
		
		// extract (movieid, rating)
		val ratings = txt.map(line => {val vs = line.split("::")
					(vs(1).toInt, vs(2).toDouble)})
		// trans to (movieid, avg)
		val movieAvg = ratings.groupByKey().map(data => { val avg = data._2.sum / data._2.size; (data._1, avg)})
		movieAvg.saveAsTextFile("avg")

		// read movies
		val movies_data = "ml-1m/movies.dat"
		val txtMovies = sc.textFile(movies_data)

		// extract (moviedid, name)
		val movies = txtMovies.map(line => {val vs = line.split("::"); (vs(0).toInt, vs(1))}).keyBy(tup => tup._1)

		// join ratings and movie
		val joins = movieAvg.keyBy(tup => tup._1).join(movies).filter(score => score._2._1._2 > 4.0).map(js => (js._1, js._2._1._2, js._2._2._2))

		// save to file
		joins.saveAsTextFile("join_result")	
	}
}
