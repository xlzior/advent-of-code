import sys
import re

with open(sys.argv[1]) as file:
    puzzle_input = file.read().strip().split("\n")

points = set()

for line in puzzle_input:
    state = line.split(" ", 1)[0]
    match = re.findall(r"(-?\d+)", line)
    start_x, end_x, start_y, end_y, start_z, end_z = map(int, match)
    if state == "on":
        for x in range(max(start_x, -50), min(end_x, 50) + 1):
            for y in range(max(start_y, -50), min(end_y, 50) + 1):
                for z in range(max(start_z, -50), min(end_z, 50) + 1):
                    points.add((x, y, z))
    else:
        for x in range(max(start_x, -50), min(end_x, 50) + 1):
            for y in range(max(start_y, -50), min(end_y, 50) + 1):
                for z in range(max(start_z, -50), min(end_z, 50) + 1):
                    if (x, y, z) in points:
                        points.remove((x, y, z))

print(len(points))