from __future__ import annotations  # for self-referential type annotations
import sys
import re

class Cuboid:
    def __init__(self, x, y, z, is_on):
        self.x = x
        self.y = y
        self.z = z
        self.is_on = is_on

    def intersect(self, other: Cuboid):
        x = max(self.x[0], other.x[0]), min(self.x[1], other.x[1])
        y = max(self.y[0], other.y[0]), min(self.y[1], other.y[1])
        z = max(self.z[0], other.z[0]), min(self.z[1], other.z[1])

        if x[0] <= x[1] and y[0] <= y[1] and z[0] <= z[1]:
            return Cuboid(x, y, z, not other.is_on)

    def size(self):
        return (1 if self.is_on else -1) *\
               (self.x[1] - self.x[0] + 1) *\
               (self.y[1] - self.y[0] + 1) *\
               (self.z[1] - self.z[0] + 1)


puzzle_input = open(sys.argv[1]).read().strip().split("\n")
originals = list()  # cuboids given in instructions
cuboids = list()    # all cuboids, including intersections with each other
count = 0

for line in puzzle_input:
    state = line.split(" ", 1)[0]
    start_x, end_x, start_y, end_y, start_z, end_z = map(int, re.findall(r"(-?\d+)", line))
    originals.append(Cuboid((start_x, end_x), (start_y, end_y), (start_z, end_z), state == "on"))

for original in originals:                          # consider / add originals in one-by-one
    for cuboid in cuboids.copy():                   # for each addition, check for intersections with all previous
        intersection = original.intersect(cuboid)
        if intersection:
            cuboids.append(intersection)
            count += intersection.size()
    if original.is_on:                              # if this was an "on" instruction, include it
        cuboids.append(original)
        count += original.size()

print(count)
