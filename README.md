# hello-scala
Experimenting with Scala and Spark-GraphX

In markets and social networks, it is useful for a business to have an idea when and how to reach and stay in full market penetration. Social network platforms for instance would like to understand the factors that help them to connect as many users as possible. In network dynamics, this can be seen as a state where almost all nodes are connected to each other. This is also the state where clustering and giant components as well as high connectivity across different communities will be observed. It has been observed that the transition to full connectivity can be sudden, and this process is called percolation. A discussion of Percolation as it relates to [random] graphs is in Section IV the paper Statistical Mechanics of Complex Networks: 
http://www.barabasilab.com/pubs/CCNR-ALB_Publications/200201-30_RevModernPhys-StatisticalMech/200201-30_RevModernPhys-StatisticalMech.pdf

In this experiment, we use Spark's GraphX libraries through Scala to evolve a network and compute a few metrics that capture the increase in interconnectivity. The metrics we'd like to measure are formation of cliques or triangles, and giant components. The more triangles go through any vertice, the more fully-connected the graph is. A fully connected graph will also have larger components, or, or one, two or a few super-communities (i.e., communities with their size being a large fraction of the total graph size).

We we grow a small random network by continually adding edges to a fixed number of vertices. We take metrics at each time step. We can then detect where sudden or spikes in the metric occur. This will be a good indicator of the percolation point or stage

Metrics:

1. Number of triangles: GraphX returns the number of triangles going through each vertex. We can rank the vertices according to the number of triangles they have and pick the first m as a metric. The total number of triangles these m vertices have can be a useful preliminary metric for the forming of giant components or super-communities. This is already part of the code.

2. Size of connected components: This can be the best metric for capturing formation of large communities or percolation. GraphX returns the connected component each vertix belongs to. We'd like to determine the component containing the highest number of vertices. This is not as yet part of the code yet but is in progress.


Output Data:
The code returns a time series of 3 vertices having highest number of triangles( see output folder), and average degree. Next to be included will be the size of the largest connected component. We can also make a change metric for detecting sudden changes in the metric. These text data can be parsed by say, Python, and visualizations done using Matplotlib

 

