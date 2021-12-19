from __future__ import annotations  # for self-referential type annotations
from itertools import combinations
import sys

class Point:
    def __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z

    def distance_to(self, other: Point):
        return abs(self.x - other.x) + abs(self.y - other.y) + abs(self.z - other.z)


with open(sys.argv[1]) as file:
    puzzle_input = file.read().strip().split("\n")

scanners = list()
for line in puzzle_input:
    x, y, z = list(map(int, line.split(',')))
    scanners.append(Point(x, y, z))

max_seen = 0
for a, b in combinations(scanners, 2):
    max_seen = max(max_seen, a.distance_to(b))
print(max_seen)