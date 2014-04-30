import java.util.Random
import org.apache.spark.util.Vector
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object SparkKMeans {	
	val R = 1000	// Scaling factor
	val rand = new Random(42)

	// generated data 
	def parseVector(line: String): Vector = {
		new Vector(line.split('\t').map(_.toDouble))
	}	
	
	// find the cloest point's index compared to a given point
	def cloestPoint(p: Vector, centers: Array[Vector]): Int = {
		var index = 0
		var cloestIndex = 0
		var cloest = Double.PositiveInfinity

		for (i <- 0 until centers.length) {
			val vCurr = centers(i)
			val tmpDist = p.squaredDist(vCurr)
			if (tmpDist < cloest) {
				cloest = tmpDist
				cloestIndex = i
			}
		}

		cloestIndex	
	}

	// do K-Means
	def main(args: Array[String]) {
		val ctx = new SparkContext("local", "SparkKMeans", System.getenv("SPARK_HOME"), SparkContext.jarOfClass(this.getClass))

		// placeholder syntax!!
		val lines = ctx.textFile("bridge.txt")
		//val data = lines.map(parseVector _).cache()
		val data = lines.map(parseVector _)

		val K = 2
		val convergeDist = 0.1

		val kPoints = data.takeSample(false, K, 42).toArray

		var tmpDist = 1.0

		while (tmpDist > convergeDist) {
			val cloest = data.map(p => (cloestPoint(p, kPoints), (p, 1)))
			cloest.first()
			val pointStats = cloest.reduceByKey {case ((x1, y1), (x2, y2)) => (x1 + x2, y1 + y2)}
			val newPoints = pointStats.map(pair => (pair._1, pair._2._1 / pair._2._2)).collectAsMap()
			tmpDist = 0.0
			for (i <- 0 until K) {
				tmpDist += kPoints(i).squaredDist(newPoints(i))
			}

			for (rs <- newPoints) {
				kPoints(rs._1) = rs._2
			}
		}

		kPoints.foreach(println)	
	}
}
