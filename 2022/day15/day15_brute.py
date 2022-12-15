import sys
import re
import collections


def distance(ax, ay, bx, by):
    return abs(ax - bx) + abs(ay - by)


puzzle_input = open(sys.argv[-1]).read().split("\n")
y = 10 if sys.argv[-1] == "sample.txt" else 2_000_000
exclusion_zones = list()
min_x = 0
max_x = 0
max_radius = 0
known_beacons = set()

for line in puzzle_input:
    sx, sy, bx, by = map(int, re.findall(r"(-?\d+)", line))
    if by == y:
        known_beacons.add((bx, by))
    radius = distance(sx, sy, bx, by)
    min_x = min(min_x, sx)
    max_x = max(max_x, sx)
    max_radius = max(max_radius, radius)
    exclusion_zones.append((sx, sy, radius))

count = -len(known_beacons)
for x in range(min_x - max_radius, max_x + max_radius + 1):
    for sx, sy, radius in exclusion_zones:
        if distance(sx, sy, x, y) <= radius:
            count += 1
            break

print("Part 1:", count)

frontier = collections.defaultdict(int)
candidates = set()


def add_to_frontier(x, y):
    space = 20 if sys.argv[-1] == "sample.txt" else 4_000_000
    if 0 <= x <= space and 0 <= y <= space:
        frontier[(x, y)] += 1
        if frontier[(x, y)] == 4:
            if all(
                distance(sx, sy, x, y) > radius for sx, sy, radius in exclusion_zones
            ):
                print((x, y))
                print("Part 2:", x * 4_000_000 + y)


for sx, sy, radius in exclusion_zones:
    target_distance = radius + 1
    for dx in range(target_distance + 1):
        dy = target_distance - dx
        add_to_frontier(sx - dx, sy - dy)
        add_to_frontier(sx - dx, sy + dy)
        add_to_frontier(sx + dx, sy - dy)
        add_to_frontier(sx + dx, sy + dy)
