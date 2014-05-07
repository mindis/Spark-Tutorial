package org.apache.spark.mllib.regression

/**
 * Class that represents the features and labels of a data point.
 *
 * @param label Label for this data point.
 * @param features List of features for this data point.
 */
case class LabeledPoint(label: Double, features: Array[Double]) {
  override def toString: String = {
    "LabeledPoint(%s, %s)".format(label, features.mkString("[", ", ", "]"))
  }
}
