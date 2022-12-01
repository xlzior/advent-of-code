import collections
import sys
import re
from Cuboid import Cuboid
from Intersection import Intersection

# This is wrong because it does not maintain the idea of chronology
# It follows the rules for Inclusion-Exclusion Principle in *one pass* over all the known cuboids
# It assumes that once a light is off, it will never be turned on again in future, which is not true

with open(sys.argv[1]) as file:
    puzzle_input = file.read().strip().split("\n")

count = 0
cuboids = list()
intersections = collections.defaultdict(set)

for id in range(len(puzzle_input)):
    line = puzzle_input[id]
    state = line.split(" ", 1)[0]
    match = re.findall(r"(-?\d+)", line)
    start_x, end_x, start_y, end_y, start_z, end_z = map(int, match)
    cuboid = Cuboid(id, start_x, end_x, start_y, end_y, start_z, end_z)
    cuboids.append(cuboid)
    if state == "on":
        count += cuboid.size()  # Include the cardinalities of the sets.
    intersections[1].add(Intersection({cuboid}, cuboid))

print(f"1-way intersection: {len(cuboids)}. count = {count}")
k = 2  # in this round we are dealing with intersections of k sets

while True:
    multiplier = -1 if k % 2 == 0 else 1
    for subintersection in intersections[k - 1]:
        for cuboid in cuboids:
            intersection = subintersection.intersect(cuboid)
            if intersection and intersection not in intersections[k]:
                intersections[k].add(intersection)
                count += multiplier * intersection.size()
    print(f"{k}-way intersections: {len(intersections[k])}. count = {count}")
    if len(intersections[k]) == 0:
        break

    k += 1

print(count)
