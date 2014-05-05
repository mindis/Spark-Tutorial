package org.apache.spark.mllib.optimization

import org.jblas.DoubleMatrix

/**
 * Class used to compute the gradient for a loss function, given a single data point.
 */
abstract class Gradient extends Serializable {
  /**
   * Compute the gradient and loss given features of a single data point.
   *
   * @param data - Feature values for one data point. Column matrix of size nx1
   *               where n is the number of features.
   * @param label - Label for this data item.
   * @param weights - Column matrix containing weights for every feature.
   *
   * @return A tuple of 2 elements. The first element is a column matrix containing the computed
   *         gradient and the second element is the loss computed at this data point.
   *
   */
  def compute(data: DoubleMatrix, label: Double, weights: DoubleMatrix): (DoubleMatrix, Double)
}

/**
 * Compute gradient and loss for a logistic loss function.
 */
class LogisticGradient extends Gradient {
  // https://work.caltech.edu/library/093.pdf
  // NOTE: Labels used in Logistic Regression should be {0, 1}
  override def compute(data: DoubleMatrix, label: Double, weights: DoubleMatrix): (DoubleMatrix, Double) = {
    val margin: Double = -1.0 * data.dot(weights)
    val gradientMultiplier = (1.0 / (1.0 + math.exp(margin))) - label

    val gradient = data.mul(gradientMultiplier)
    val loss =
      if (label > 0) {
        math.log(1 + math.exp(margin))
      } else {
        math.log(1 + math.exp(margin)) - margin
      }

    (gradient, loss)
  }
}

/**
 * Compute gradient and loss for a Least-squared loss function.
 */
class SquaredGradient extends Gradient {
  override def compute(data: DoubleMatrix, label: Double, weights: DoubleMatrix): (DoubleMatrix, Double) = {
    val diff: Double = data.dot(weights) - label

    val loss = 0.5 * diff * diff
    val gradient = data.mul(diff)

    (gradient, loss)
  }
}

/**
 * Compute gradient and loss for a Hinge loss function.
 * NOTE: This assumes that the labels are {0,1}
 */
class HingeGradient extends Gradient {
  override def compute(data: DoubleMatrix, label: Double, weights: DoubleMatrix): (DoubleMatrix, Double) = {
    val dotProduct = data.dot(weights)

    // Our loss function with {0, 1} labels is max(0, 1 - (2y 鈥�1) (f_w(x)))
    // Therefore the gradient is -(2y - 1)*x
    val labelScaled = 2 * label - 1.0

    if (1.0 > labelScaled * dotProduct) {
      (data.mul(-labelScaled), 1.0 - labelScaled * dotProduct)
    } else {
      (DoubleMatrix.zeros(1, weights.length), 0.0)
    }
  }
}
