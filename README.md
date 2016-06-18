# hello-scala
Experimenting with Scala and Spark-GraphX

In markets and social networks, it is useful for a business to have an idea when and how to reach and stay in full market penetration. Social network platforms for instance would like to understand the factors that help them to connect as many users as possible. In network dynamics, this can be seen as a state where almost all nodes are connected to each other. This is also the state where clustering and giant components as well as high connectivity across different communities will be observed. It has been observed that the transition to full connectivity can be sudden, and this process is called percolation. A discussion of Percolation as it relates to [random] graphs is in Section IV the paper Statistical Mechanics of Complex Networks: 
http://www.barabasilab.com/pubs/CCNR-ALB_Publications/200201-30_RevModernPhys-StatisticalMech/200201-30_RevModernPhys-StatisticalMech.pdf

In this experiment, we use Spark's GraphX libraries through Scala to evolve a network and compute a few metrics that capture the increase in interconnectivity. The metrics we'd like to measure are formation of cliques or triangles, and giant components. The more triangles go through any vertice, the more fully-connected the graph is. A fully connected graph will also have larger components, or, or one, two or a few super-communities (i.e., communities with their size being a large fraction of the total graph size).

We grow a small random network by continually adding edges to a fixed number of vertices. We take metrics at each time step. We can then detect where sudden or spikes in the metric occur. This will be a good indicator of the percolation point or stage

Metrics: 

Number of triangles and Size of largest connected components metrics are coded

1. Number of triangles: GraphX returns the number of triangles going through each vertex. We can rank the vertices according to the number of triangles they have and pick the first m as a metric. The total number of triangles these m vertices have can be a useful preliminary metric for the forming of giant components or super-communities. 

2. Size of connected components: This can be the best metric for capturing formation of large communities or percolation. GraphX returns the connected component each vertix belongs to. We determine the component containing the highest number of vertices by using the RDD actions and transformations of Spark 


Output Data:
(See output folder - log.txt). The code returns a time series of 3 vertices having highest number of triangles(the number of triangles is the 2nd element of the pair), and average degree, and the size of the largest component (2nd element of pair). These text data can be parsed by say, Python, and visualizations done using Matplotlib

Performance Estimates:
T = number of events/runs
N = number of vertices = constant
e = edge rate = constant
G =  size of graph = t * N ( = T*N i.e., worst case) 
Complexity = O(form graph) + O(collect data) =  O( T*T*N) + O(N*T) ~= (T*T)
=> Complexity = O(T*T) 
When/if we update the graph by only adding new edges instead of reconstructing entire graph each time, then 
Complexity = O(T)
            
 

