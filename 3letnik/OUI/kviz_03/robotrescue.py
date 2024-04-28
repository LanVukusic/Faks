import math
import cProfile
import search


class RobotRescue:
    def __init__(self, grid, robot, wounded):
        # 0 - empty space
        # 1 - obstacle
        self.grid = grid
        self.robot = robot
        self.wounded = wounded

    def solved(self):
        return self.robot == self.wounded

    def move(self, move):
        self.robot[0] += move[0]
        self.robot[1] += move[1]

    def undo_move(self, move):
        self.robot[0] -= move[0]
        self.robot[1] -= move[1]

    def generate_moves(self):
        row, col = self.robot
        moves = []
        for dr in [-1, 0, 1]:
            for dc in [-1, 0, 1]:
                if dr == 0 and dc == 0:
                    continue
                if row+dr < 0 or row+dr == len(self.grid) or \
                   col+dc < 0 or col+dc == len(self.grid[row+dr]):
                    continue
                if self.grid[row+dr][col+dc] == 0:
                    move = [dr, dc]
                    cost = math.sqrt(dr**2 + dc**2)
                    moves.append((move, cost))
        return moves

    def ID(self):
        return tuple(self.robot)

    def evaluate(self):
        return 0


grid = [[0, 0, 0, 0, 0, 0],
        [1, 0, 1, 1, 1, 0],
        [0, 0, 1, 0, 0, 0],
        [0, 1, 1, 0, 0, 0],
        [0, 1, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0]]

rr = RobotRescue(grid, [3, 3], [3, 0])
resitev = search.DF(rr, 100)
print(resitev)

cProfile.run('search.A(rr)')
