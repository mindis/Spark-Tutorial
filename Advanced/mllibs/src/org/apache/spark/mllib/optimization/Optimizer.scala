package org.apache.spark.mllib.optimization

import org.apache.spark.rdd.RDD

trait Optimizer {

  /**
   * Solve the provided convex optimization problem.
   */
  def optimize(data: RDD[(Double, Array[Double])], initialWeights: Array[Double]): Array[Double]

}
