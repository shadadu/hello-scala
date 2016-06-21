import java.io.PrintWriter
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import org.apache.spark.{SparkConf, SparkContext}
import scala.util.Random



object Hello  {

  def randomEdges(rndm: scala.util.Random, N: Int, nTries:Int): List[Int] = {
    /*
     Generate pair of random numbers from 0 to N-1 to pick pair of vertices to connect with an undirected edge.
     If the two vertices are the same, pick a new pair of vertices to connect
     */
    var v1 = rndm.nextInt(N)
    var v2 = rndm.nextInt(N)
    var count = 0
    while (v1 == v2 && count <= nTries ) {
      v1 = rndm.nextInt(N.toInt)
      v2 = rndm.nextInt(N.toInt)
      count+=1
    }
    List(v1,v2)
  }

  def max2(a: (Long, Long), b: (Long, Long)): (Long, Long) = if (a._2 > b._2) a else b


  def percolation(N: Int, T: Int, nTries: Int, sc: SparkContext, edgeRate: Int, rndm: Random, randomEdges: (scala.util.Random, Int, Int) => List[Int], writer: PrintWriter): Unit = {

    /*
    initialize N vertices; each vertex has 2 attributes: Id (Long), name(String)
    */
    val verticesTemp = ListBuffer[(Long, String)]()
    for (i <- 0 until N) {
      verticesTemp += ((i, i.toString + "th"))
    }
    val vRDD = sc.parallelize(verticesTemp.toList) // form RDD of the vertices

    /*
    Initialize a number of edges. EdgeRDD is mutable to enable adding new edges
    GraphX doesn't explicitly allow directed edges, so make the edges undirected by
    forming two reversing edges between the two vertices.
    */
    var EdgeRDD: RDD[Edge[PartitionID]] = sc.parallelize(Array(Edge(0L, 0L, 1), Edge(0L, 0L, 1)))

    /*
    Run simulation of T events. During each t step, add one (undirected) edge between a randomly-selected
    pair of existing vertices
     */
    writer.write("t,maxTris,avgDegree,maxCCsize\n")

    for (t <- 0 until T ) {
      /*
      To add edgeRate number of new edges during each t, make a bucket to hold new edges that are randomly generated.
      Initialize the bucket with one new randomly edge. bucket size allows to set the rate of increase in size of network
      time per step
       */
      val newEdges = randomEdges(rndm,N,nTries)
      var v1 = newEdges.head
      var v2 = newEdges(1)
      val nextEdge: ArrayBuffer[Edge[PartitionID]] = ArrayBuffer(Edge(v1.toLong, v2.toLong, 1), Edge(v2.toLong, v1.toLong, 1))
      val nextEdgeRDD: RDD[Edge[PartitionID]] = sc.parallelize(nextEdge)
      var edgeBucketRDD = nextEdgeRDD
      val edgeBucket: ArrayBuffer[Edge[PartitionID]] = nextEdge
      // Add remaining edgeRate-1 randomly generated edges to the bucket
      for (n <- 1 to edgeRate) {
        val currEdges = randomEdges(rndm, N, nTries)
        v1 = currEdges.head
        v2 = currEdges(1)
        edgeBucket +=(Edge(v1.toLong, v2.toLong, 1), Edge(v2.toLong, v1.toLong, 1))
        edgeBucketRDD = sc.parallelize(edgeBucket)
      }
      // Add the bucket of edges to the collection (RDD) of existing edges;
      // i.e. update EdgeRDD with the newly formed edges
      EdgeRDD = sc.union(EdgeRDD, edgeBucketRDD)
      /*
        Make the graph
     */

      val graph = Graph(vRDD, EdgeRDD, "").persist()
      /*
      collect data and get metrics
       */
      val cc = graph.connectedComponents().vertices
      val triCounts = graph.triangleCount().vertices
      val avgDegree = graph.numEdges/N.toDouble
      graph.unpersist()
      val rankTris: Int = 3
      val maxTris = triCounts.collect.sortWith(_._2 > _._2).take(rankTris)
      val maxTrisSum = maxTris.map(c => c._2).sum
      val maxCC= cc.keyBy(_._2).countByKey.reduce(max2)

      writer.write(t+","+ maxTrisSum.toString+","+avgDegree+","+maxCC._2.toString+"\n")
    }


  }


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("hello-spark").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val T: Int = 100 // the number of events to run; the evolution time of the dynamics
    val N: Int = 100 // the number of vertices
    val edgeRate: Int = 5 // the number of edges to add during each time step
    val nTries: Int = 5   // the number of times to avoid self-links; to avoid infinite while loop
    val rndm = scala.util.Random
    val writer = new PrintWriter("output\\outputData.csv")

    percolation(N,T,nTries,sc,edgeRate,rndm, randomEdges, writer)

    writer.close()

    sc.stop()
  }

}
