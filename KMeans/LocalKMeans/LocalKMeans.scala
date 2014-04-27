import java.util.Random
import org.apache.spark.util.Vector
import org.apache.spark.SparkContext
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

object LocalKMeans {
	val N = 1000
	val R = 1000	// Scaling factor
	val D = 10
	val K = 10
	val convergeDist = 0.001
	val rand = new Random(42)

	// generated data 
	def generateData = {
		def generatePoint(i: Int) = {
			Vector(D, _ => rand.nextDouble * R)
		}	
		Array.tabulate(N)(generatePoint)	
	}
	
	// find the cloest point's index compared to a given point
	def cloestPoint(p: Vector, centers: HashMap[Int, Vector]): Int = {
		var index = 0
		var cloestIndex = 0
		var cloest = Double.PositiveInfinity

		for (i <- 1 to centers.size) {
			val vCurr = centers.get(i).get
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
		val data = generateData
		var points = new HashSet[Vector]
		var kPoints = new HashMap[Int, Vector]
		var tmpDist = 1.0

		// generate random points as initial centers
		while(points.size < K)	points.add(data(rand.nextInt(N)))
		
		// put initial centers into HashMap
		val iter = points.iterator
		for (i <- 1 to points.size) kPoints.put(i, iter.next())

		println("Initial centers: " + kPoints)

		while (tmpDist > convergeDist) {
			// find each point's cloest points from kPoints
			var cloest = data.map(p => (cloestPoint(p, kPoints), (p, 1)))

			// group points by centers' indices
			var mappings = cloest.groupBy[Int] (x => x._1)

			// obtain each group's  centroid
			var pointStats = mappings.map(pair => pair._2.reduceLeft[(Int, (Vector, Int))] {
				case ((id1, (x1, y1)), (id2, (x2, y2))) => (id1, (x1 + x2, y1 + y2))})
			
			// obtain new centers
			var newPoints = pointStats.map {mapping => (mapping._1, mapping._2._1 / mapping._2._2)}
			tmpDist = 0.0
			for (mapping <- newPoints) {
				tmpDist += kPoints.get(mapping._1).get.squaredDist(mapping._2)
			}

			// reassign centers
			for (rp <- newPoints)
				kPoints.put(rp._1, rp._2)	
		}
		
		println("Final centers: " + kPoints)
	}
}
