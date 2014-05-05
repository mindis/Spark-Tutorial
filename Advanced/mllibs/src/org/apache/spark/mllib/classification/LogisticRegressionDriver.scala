package org.apache.spark.mllib.classification

import org.apache.spark.SparkContext
import org.apache.spark.mllib.regression.LabeledPoint

object LogisticRegressionDriver {

  def main(args: Array[String]) {}
  val sc = new SparkContext("local", "SparkLR", System.getenv("SPARK_HOME"), SparkContext.jarOfClass(this.getClass))
  // Load and parse the data file
  val data = sc.textFile("data/lr_data.txt")
  val parsedData = data.map { line =>
    val parts = line.split(' ')
    LabeledPoint(parts(0).toDouble, parts.tail.map(x => x.toDouble).toArray)
  }

  // Run training algorithm to build the model
  val numIterations = 20
  val model = SVMWithSGD.train(parsedData, numIterations)

  // Evaluate model on training examples and compute training error
  val labelAndPreds = parsedData.map { point =>
    val prediction = model.predict(point.features)
    (point.label, prediction)
  }
  val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / parsedData.count
  println("Training Error = " + trainErr)
}