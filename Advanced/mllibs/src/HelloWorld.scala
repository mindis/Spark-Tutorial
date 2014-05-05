import org.jblas.DoubleMatrix
object HelloWorld {
	def main(args: Array[String]) {
	  val as = 1.0::2.0::3.0::Nil toArray
	  
	  println(as)
	  
	  val weightsMatrix = new DoubleMatrix(as.length, 1, as: _*)
	  
	  weightsMatrix.print();
	  
	  val ass = 1 +: as
	  for (a <- ass)
		 println(a)
	}
}