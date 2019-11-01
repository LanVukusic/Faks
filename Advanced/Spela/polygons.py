# Let P be a simple closed polygon in the plane. A polygon is a plane figure that is bounded
# by a finite chain of straight line segments closing in a loop to form a closed polygonal chain.
# These segments are called its edges or sides, and the points where two edges meet are the
# polygon s vertices or corners. A polygon is simple if it does not have self-intersections.
# We triangulate P , possibly adding vertices in the interior.

#https://docs.google.com/document/d/1UDTe8hj_vmUN-GfhZPcwbZGfrS1FxMWGxmrwWJDY9yA/edit#

class Node():
    def __init__(self, ids):
        self.nodeID = ids
        self.siblings = []
    
    def get_siblings(self):
        return self.siblings

    def add_sibling(self, a):
        self.siblings.append(a)

class Graph():
    def __init__(self, t):
        self.node_dict = {}
        self.create_graph(t)
    
    def create_graph(self, t):
        for triangle in T:
            for i in range(3):
                if (not triangle[i] in self.node_dict.keys()):
                    # the triangle[i] has not been added yet
                    self.node_dict[triangle[i]] = Node(triangle[i])  # we add the node to the dict
                    self.node_dict[triangle[i]].add_sibling(triangle[(i+1)%3])  # add the two connected nodes to the list
                    self.node_dict[triangle[i]].add_sibling(triangle[(i+2)%3])
    
    def Triangulate(self):
        pass
        # start with the first edge... we know "1" will be there somewhere

    def DebugMe(self):
        print(self.node_dict)

T = [(1,2,6),(1,5,6),(2,3,7),(2,6,7),(3,4,8),(3,7,8),(5,6,9),(6,7,11),
(6,9,10),(6,10,11),(7,8,12),(7,11,12),(9,10,13),(10,13,14),
(10,11,15),(10,14,15),(11,12,15),(12,15,16)]

lol = Graph(T)
lol.DebugMe()


def shelling(T):
    # T being the list of polygons
    pass