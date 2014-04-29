object Pagerank {
  type UrlId = Int

  def pagerank(links: Array[(UrlId, Array[UrlId])], numIters: Int):Map[UrlId, Double] = {
    val n = links.size
    var ranks = (Array.fromFunction(i => (i, 1.0)) (n)).toMap  
    
    for (iter <- 1 to numIters) {
      val contrib =
        links
        .flatMap(node => {
	          val out_url = node._1
	          val in_urls = node._2
	          val score = ranks(out_url) / in_urls.size // when size=0, double/0=Infinity
	          in_urls.map(in_url => (in_url, score) )
          }
          )
        .groupBy(url_score => url_score._1)
        .map(url_scoregroup => 
          (url_scoregroup._1, url_scoregroup._2.foldLeft (0.0) ((sum,url_score) => sum+url_score._2)))
    		 
         
      ranks = ranks.map(url_score => (url_score._1, if (contrib.contains(url_score._1)) 0.85 * contrib(url_score._1) + 0.15  else url_score._2))
    }
    ranks
  }
}

object pr_data {
  // spark part-2 page 15
  // http://ampcamp.berkeley.edu/amp-camp-one-berkeley-2012/
  val sample1 = Array((0, Array(1)), (1, Array(0,2)), (2, Array(0,3)), (3, Array(0)))
  
  
  // example from http://langvillea.people.cofc.edu/PRDataCode/index.html?referrer=webcluster&
  val complexity = 
    	io.Source.fromURL("http://www.cs.toronto.edu/~tsap/experiments/datasets/computational_complexity/expanded/adj_list")
    	.getLines.toArray
    	.map(line => {
    	  val i = line.split(':')(0).toInt
    	  val s = line.split(':')(1).trim().split(' ') // take string after ':' and split
    	          .map(v => v.toInt)
    	          .filter(v => v >=0 )
    	  (i,s)
    	})
    	
}

object Test {
  def main(args: Array[String]): Unit = {
    Pagerank.pagerank(pr_data.complexity, 100)
    
  }
}
