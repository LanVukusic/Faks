# INA izpiski

## From graph theory to network science

[Barabashi chapter 2](http://networksciencebook.com/chapter/2)

### Degrees

- **Degree** Number of links a node has
- **Average degree** avg of degrees of all nodes
- **Degree distribution** Normalized histogram of degrees of nodes

We diferentiate between **directed** and **undirected** graphs.  
Directed graphs have `in-degree` and `out-degree`.  

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

**Shortest path** is a path with fewest links. !! Importantly, a shortest path from A to B is may not be the same as B to A in directed networks.
It can be calculated from the adjacency matrix, but more often **BFS** is used due to better performance.  

