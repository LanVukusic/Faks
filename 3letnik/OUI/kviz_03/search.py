# basic search techniques
# written by Aleksander Sadikov, November 2007 (and updated in November 2008 and March 2010)
# for AI practical classes @ University of Ljubljana
# changed by Martin Mozina, October 2011: using deque instead of list, not removing candidates in A*
# changed by Martin Mozina, 2015: converted to Python 3, stylistic changes, added AO*
# warning: all algorithms were written for educational purposes, therefore not optimized.

import heapq
import collections
INFINITY = 1000000

def execute_sequence(pos, sequence):
    for move, cost in sequence:
        pos.move((move, cost))

def undo_sequence(pos, sequence):
    for move, cost in reversed(sequence):
        pos.undo_move((move, cost))

def DF(pos, depth):
    # depth-first search
    if depth < 0: return None
    if pos.solved(): return []
    for move, cost in pos.generate_moves():
        pos.move(move)
        found_path = DF(pos, depth-1)
        pos.undo_move(move)
        if found_path != None:
            found_path.insert(0, (move, cost))
            return found_path
    return None

def DF_basic(pos):
    # depth-first search
    if pos.solved(): return []
    for move, cost in pos.generate_moves():
        pos.move(move)
        found_path = DF_basic(pos)
        pos.undo_move(move)
        if found_path != None:
            found_path.insert(0, (move, cost))
            return found_path
    return None

def ID(pos):
    # iterative deepening
    depth = 0
    while True:
        path = DF(pos, depth)
        if path != None:
            return path
        depth += 1
        
def BF(pos):
    # breadth-first search
    cand_paths = collections.deque()
    cand_paths.append([])
    while cand_paths:
        path = auxBF(pos, cand_paths)
        if path != None: return path
    return None     # no more candidate paths and solution was not found

def auxBF(pos, cand_paths):
    # auxiliary routine for expanding nodes in breadth-first search
    path = cand_paths.popleft()
    execute_sequence(pos, path)
    if pos.solved():
        undo_sequence(pos, path)
        return path
    for move, cost in pos.generate_moves():
        cand_paths.append(path + [(move, cost)])
    undo_sequence(pos, path)
    return None

def A(pos, limit = 25000):
    # A* best-first heuristic search
    # cand_paths: a priority queue of candidate paths;
    # visited: dictionary of already generated nodes and best g-values (and f-values) seen to reach them (thus no duplicates!)
    cand_paths = [(pos.evaluate(), [], pos.ID())]
    visited = { pos.ID(): (0, pos.evaluate()) }
    ix = 1
    while cand_paths:
        if ix > limit: return None     # limit on expanded nodes reached
        ix += 1

        val, path, id = heapq.heappop(cand_paths)

        execute_sequence(pos, path)
        if pos.solved():
            undo_sequence(pos, path)
            return path

        if visited[id][1] < val:
            undo_sequence(pos, path)
            continue
        # generate candidates
        for move, cost in pos.generate_moves():
            pos.move((move, cost))
            new_id = pos.ID()
            if new_id not in visited or visited[new_id][0] > visited[id][0] + cost:
                # if a better path to state is found, add it to candidates
                visited[new_id] = (visited[id][0] + cost, visited[id][0] + cost + pos.evaluate())
                heapq.heappush(cand_paths, (visited[new_id][1], path + [(move, cost)], new_id))
            pos.undo_move((move, cost))
        undo_sequence(pos, path)

    return None     # no more candidate paths and solution was not found

def IDA(pos):
    # iterative deepening A*
    fbound = pos.evaluate()
    while True:
        res = rIDA(pos, 0, fbound)
        if isinstance(res, list):
            return res
        fbound = res

def rIDA(pos, g, fbound):
    # recursive depth-first search with F-bound for use with IDA*
    # if goal is found it returns path-to-goal (as a list of moves)
    # else it returns a new fbound candidate (as a number; this is at the same time a signal of failure)
    f = g + pos.evaluate()
    if f > fbound:
        return f

    if pos.solved():
        return []

    newfbound = INFINITY
    for move, cost in pos.generate_moves():
        pos.move(move)
        res = rIDA(pos, g + cost, fbound)
        pos.undo_move(move)
        if type(res) == type([]):
            res.insert(0, (move, cost))
            return res
        else:
            if res < newfbound: newfbound = res
    return newfbound


def RBFS(pos):
    # recursive best-first search (top level routine; it starts the recursive chain)
    return rRBFS(pos, 0, pos.evaluate(), INFINITY)

def rRBFS(pos, g, F, bound):
    # recursive best-first search algorithm (RBFS)
    # if goal is found it returns path-to-goal (as a list of moves)
    # else it returns a new bound (as a number; this is at the same time a signal of failure)
    f = g + pos.evaluate()
    if f > bound: return f
    if pos.solved(): return []

    Fs = [(INFINITY, None)]     # acts as a guard for insertion below and solves single-child case
    for move, cost in pos.generate_moves():
        pos.move(move)
        newf = g + cost + pos.evaluate()
        if f < F: newf = max(F, newf)
        pos.undo_move(move)
        insMoveByFValue(newf, (move, cost), Fs)
        
    if Fs == [(INFINITY, None)]: return INFINITY        # no legal moves in this position
    while Fs[0][0] <= bound and Fs[0][0] < INFINITY:
        F1, (move, cost) = Fs.pop(0)
        pos.move(move)
        res = rRBFS(pos, g + cost, F1, min(bound, Fs[0][0]))  # after pop(), Fs[0] is the second candidate move!
        pos.undo_move(move)
        if type(res) == type([]): return [(move, cost)] + res   # solution found; add current move to it and return
        insMoveByFValue(res, (move, cost), Fs)
    return Fs[0][0]

def insMoveByFValue(newf, move, Fs):
    # insert the pair (f-value, move) into a sorted list
    # NOTE: move with f-value of INFINITY is not inserted at all (does not change the behaviour of the algorithm!)
    for ix, (cf, cmove) in enumerate(Fs):
        if newf < cf:
            Fs.insert(ix, (newf, move))
            break

class AONode:
    def __init__(self, pos):
        # each node is marked as solved or not solved
        # each node has estimated H-value (which is INFINITY if that branch is
        # unsolvable
        self.solved = False
        self.expanded = False
        self.children = []
        self.isor = pos.isor()
        self.H = pos.evaluate()
        self.ID = pos.ID()

    def expand(self, pos, path):
        if self.solved or self.H == INFINITY:
            return
        if self.ID in path:
            return
        if not self.expanded:
            self.expanded = True
            if pos.solved():
                self.solved = True
                self.H = 0
            else:
                moves = pos.generate_moves()
                if not moves:
                    self.H = INFINITY
                    return
                for mc in moves:
                    m, c = mc
                    pos.move(m)
                    if pos.ID() in path:
                        new_node = AONode(pos)
                        new_node.H = INFINITY
                        self.children.append((mc, new_node))
                    else:
                        self.children.append((mc, AONode(pos)))
                    pos.undo_move(m)
        else:
            # find best node to expand
            # OR node: check lowest node
            # AND node: select first unsolved node
            if self.isor:
                lw_mc, lw_node = min(self.children, key = lambda x: x[0][1] + x[1].H)
                m, c = lw_mc
                pos.move(m)
                lw_node.expand(pos, path + [self.ID])
                pos.undo_move(m)
            else:
                for mc, node in self.children:
                    m, c = mc
                    if not node.solved:
                        pos.move(m)
                        node.expand(pos, path + [self.ID])
                        pos.undo_move(m)
                        break

    def evaluate(self):
        ''' Estimate difficulty of each node in search tree. '''
        if not self.children:
            return 

        candidates = []
        for mv, node in self.children:
            node.evaluate()

        if self.isor:
            self.H = min(mc[1] + node.H for mc, node in self.children)
            self.solved = any(node.solved for mc, node in self.children)
        else:
            self.H = sum(mc[1] + node.H for mc, node in self.children)
            self.solved = all(node.solved for mc, node in self.children)

    def to_str(self, spaces):
        '''Return string representation using spaces. '''
        result = ''
        result += ' '*spaces + self.ID + ',' + str(self.H) + ',' + \
                  ('AND' if not self.isor else 'OR') + ',' + str(self.solved) + ',' + str(self.expanded) + '\n'
        for mc, node in self.children:
            result += node.to_str(spaces+2)
        return result

    def __str__(self):
        ''' Returns string representation of this node and its
        antecesors. '''
        return self.to_str(0)

    def get_best_solution(self, acc, ids = [], spaces = 0):
        ids = ids + [self.ID]
        if not self.children:
            acc.append(ids)
            return

        if self.isor:
            # select the one with lowest value
            lw_mc, lw_node = min(self.children, key = lambda x: x[0][1] + x[1].H)
            lw_node.get_best_solution(acc, ids = ids, spaces = spaces + 2)
        else:
            for mc, node in self.children:
                node.get_best_solution(acc, ids = ids, spaces = spaces + 2)

def AO(pos, verbose=0):
    print(verbose)
    # create root node of search tree
    root = AONode(pos)
    while not root.solved and root.H < INFINITY:
        if verbose==1:
            print (root)
        root.expand(pos, [])
        root.evaluate()
    if verbose==1:
        print (root)
    if root.H >= INFINITY:
        return None
    else:
        res = []
        root.get_best_solution(res)
        return res
