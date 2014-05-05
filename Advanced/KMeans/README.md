### Notes about KMeans algorithm   
1. Some summary   
	algo guideline 
	weakness:
		the initialization of K(by KMeans++)
		the local limitations
	notes!:
		GMM -> EM
		KMeans -> GMM -> EM
	parallelization scheme
	time complexity
	space complexity
	speed-up ratio

### KeyPoint about the code   
1. how data flow	
2. the data structure
	data store in RDDs, centers in Array[Array[Double]] which was copied to each worker	
3. parallel segment
	Partition data via mapPartitions, and find the bestCenter index, the sum and count of points mapping to each center
