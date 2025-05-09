# INA izpiski

## From graph theory to network science

[Barabashi chapter 2](http://networksciencebook.com/chapter/2)

### Degrees

- **Degree** Number of links a node has
- **Average degree** avg of degrees of all nodes
- **Degree distribution** Normalized histogram of degrees of nodes

We diferentiate between **directed** and **undirected** graphs.  
Directed graphs have `in-degree` and `out-degree`.  

Networks become **super-critical** when $1 \lt k \lt ln(n)$.

### Adjacency matrix

Square matrix of shape $N \times N$ where each cell represents an edge between $i$ th and $j$ th node.  

- In weighted networks those numbers can be any real number
- In "normal" networks, 1 represents and edge and 0 no edge.

Due to the sparsity of large real world networks, the adjacency matrix is also sparse (lots of 0s) and is thusly rearely used, due to its inefficient storage.  

![Real network sparsity example](http://networksciencebook.com/images/ch-02/figure-2-7.jpg)

### Weighted networks

Netowrk, where each edge has a weight asociated with it.  
Usefull for represeting a wide range of real world entities and relations (paths weights, connection importance, ...)  

Most algorithms that work for unweighted networks apply to weighted ones with just slight modifications.

### Bipartite networks

Bipartite or $N$-partite networks, are networks where nodes can be divided into $N$ (2) disjoint sets of nodes such that each node from `U` connects to a node from `V` and vice-versa.  
We can imagine coloring each node type with a seperate color and allowing only different colored nodes to connect to eachother.  

We can create a projection to `U` - a node type, which will collapse other nodes and connect two `U` nodes, whenever they are connected to the same `V` node.  

![Bipartite projection](http://networksciencebook.com/images/ch-02/figure-2-9.jpg)

### Paths & distances

A `Path` between nodes **A** and **B** is an ordered set of links connecting them.  đ
Paths **lengths** is the size of this set (aka, number of links).  

- **Shortest path** is a path with fewest links. **!! Importantly**, a shortest path from A to B is may not be the same as B to A in directed networks.
It can be calculated from the adjacency matrix, but more often **BFS** is used due to better performance.  
- **Diameter** The longest shortest path in the graph.  
- **AVG path length** Average of all shortest paths between nodes.  
- **Cycle** Loop...obvious  
- **Eulerian path** traverses each link only once  
- **Hamiltonian path** traverses each node only once.  

### Connectedness

- **Connected componnent** is a set of nodes that have a path inbetween them.  
- **Clustering coefficient**
  - **Local** clustering coefficient tells you how many of the connected nodes, form a triangle.
  - **Global** clustering operates on triplets of nodes in the graph.

## Graphs

There are several types of graphs:

- **Graph**
  - set of link and nodes
  - **Directed** - links have a direction
  - **Undirected** - links go both ways

**Density**: Graphs are considered dense if the number of links is close to all possible links.

## Random models

### Erods - Reny

Erdos - Reny **G(n, m)** random graph. is constructed by creating $n$ nodes and just randomly throwing $m$ links at it.  

- Does not have hubs with large $k$.
- When $k \gt 1$ a large connected componnent emerges.

### Configuration model

Model **G(k)**.  
Generates graph based on the available **stubs**.  

- **Neighbour** degree distribution is not the same as degree distribution
  - as in social networks - **Friendship paradox**
- Clustering coefficient resembles real networks

### Small world model

Model **G(n, k, p)**

- randomly rewire $\frac{pnk}{2}$ links of regular lattice

#### Small world netowrks

- High clustering
- small distances
- 6 degree of seperation phenomenon

### Preferential model

**G(n, c)** or **G(n, c, a)** models.

- the more connected a node is, the more likely it is to receive new links
- real world degree dist

#### Scale Free networks

Scale free networks follow a powerlaw degree distribution.

## Node mixing

- Asortative mixing
  - clusters same or simmilar nodes
- Disasortative mixing
  - custers different nodes together (sex graph - 2 gener type nodes)
- $K_{nn}(k)$ is average degree of nodes with $k$ degree.
- $P(k'|k)$ Degree linking probability
- $\mu$ = degree mixing power law
- $r$ degree mixing coefficient
  - $r$ and $\mu$ correlate in assortative netowrks
