package org.apache.spark.mllib.clustering

import org.apache.spark.SparkContext
object KMeansDriver {
  def main(args: Array[String]) {}
  // Load and parse the data
  val sc = new SparkContext("local", "SparkLR", System.getenv("SPARK_HOME"), SparkContext.jarOfClass(this.getClass))

  val data = sc.textFile("bridge.txt")
  val parsedData = data.map(_.split('\t').map(_.toDouble))

  // Cluster the data into two classes using KMeans
  val numIterations = 20
  val numClusters = 2
  val runs = 7
  
  val startTime = System.currentTimeMillis
  
  val clusters = KMeans.train(parsedData, numClusters, numIterations,runs)
  
  val endTime = System.currentTimeMillis

  // Evaluate clustering by computing Within Set Sum of Squared Errors
  val WSSSE = clusters.computeCost(parsedData)
  println("Within Set Sum of Squared Errors = " + WSSSE)
  println("escaped time: " + (endTime - startTime))
}