from collections import defaultdict
  
#Using adjacency list representation, This class represents an undirected graph

class Graph:
  
    def __init__(self,vertices):
        self.V= vertices #No. of vertices
        self.graph = defaultdict(list) # default dictionary to store graph
        self.Time = 0
  
    # function to add an edge to graph
    def addingEdge(self,u,v):
        self.graph[u].append(v)
        self.graph[v].append(u)
  
    '''A recursive function that finds and prints bridges
    using DFS traversal
    
    u --> The vertex to be visited next
    visited[] --> A list, which keeps tract of visited vertices
    disc[] --> A list, which Stores discovery times of visited vertices
    parent[] --> A list, which Stores parent vertices in DFS tree'''
    
    def bridgeDetails(self,u, visited, parent, low, disc):
        visited[u]= True
        disc[u] = self.Time
        low[u] = self.Time
        self.Time += 1
 
        #Recursive execution for all the vertices adjacent to this vertex
        for v in self.graph[u]:
            print(v)
            if visited[v] == False :
                parent[v] = u
                self.bridgeDetails(v, visited, parent, low, disc)

                low[u] = min(low[u], low[v])
  
                ''' If the lowest vertex reachable from subtree
                under v is below u in DFS tree, then u-v is
                a bridge'''
                if low[v] > disc[u]:
                    print ("%d--%d" %(u,v))
                          
            elif v != parent[u]: 
                # Update low value of u for parent function calls.
                low[u] = min(low[u], disc[v])
  
    # DFS based function to find all bridges. It uses recursive
    # function bridgeDetails()
    def findAllBridge(self):
  
     # Mark all the vertices as not visited and Initialize parent and visited
        visited = [False] * (self.V)
        disc = [float("Inf")] * (self.V)
        low = [float("Inf")] * (self.V)
        parent = [-1] * (self.V)
 
        # to find bridges in DFS tree rooted with vertex 'i'
        for i in range(self.V):
            if visited[i] == False:
                self.bridgeDetails(i, visited, parent, low, disc)
  
  
# Program Execution starts from here
if __name__ == '__main__':  

    # Creating a graph given in the above diagram (Fig a)
    g = Graph(7)
    g.addingEdge(0, 7)
    g.addingEdge(7, 6)
    g.addingEdge(7, 5)
    g.addingEdge(6, 5)
    g.addingEdge(5, 3)
    g.addingEdge(3, 1)
    g.addingEdge(3, 4)
    
    print("\nBridges in the graph are:")
    g.findAllBridge()

