from __future__ import annotations  # for self-referential type annotations
import sys
import re

class Cuboid:
    def __init__(self, start_x, end_x, start_y, end_y, start_z, end_z, is_on):
        self.start_x = start_x
        self.end_x = end_x
        self.start_y = start_y
        self.end_y = end_y
        self.start_z = start_z
        self.end_z = end_z
        self.is_on = is_on

    def intersect(self, other: Cuboid):
        start_x = max(self.start_x, other.start_x)
        start_y = max(self.start_y, other.start_y)
        start_z = max(self.start_z, other.start_z)
        end_x = min(self.end_x, other.end_x)
        end_y = min(self.end_y, other.end_y)
        end_z = min(self.end_z, other.end_z)

        if start_x <= end_x and start_y <= end_y and start_z <= end_z:
            return Cuboid(start_x, end_x, start_y, end_y, start_z, end_z, not other.is_on)

    def size(self):
        return (1 if self.is_on else -1) *\
               (self.end_x - self.start_x + 1) *\
               (self.end_y - self.start_y + 1) *\
               (self.end_z - self.start_z + 1)


with open(sys.argv[1]) as file:
    puzzle_input = file.read().strip().split("\n")

originals = list()  # cuboids given in instructions
cuboids = list()    # all cuboids, including intersections with each other
count = 0

for line in puzzle_input:
    state = line.split(" ", 1)[0]
    start_x, end_x, start_y, end_y, start_z, end_z = map(int, re.findall(r"(-?\d+)", line))
    originals.append(Cuboid(start_x, end_x, start_y, end_y, start_z, end_z, state == "on"))

for original in originals:                          # add originals in one-by-one
    for cuboid in cuboids.copy():                   # for each addition, check for intersections with all previous
        intersection = original.intersect(cuboid)
        if intersection:
            cuboids.append(intersection)
            count += intersection.size()
    if original.is_on:                              # if this was an "on" instruction, include it
        cuboids.append(original)
        count += original.size()

print(count)
