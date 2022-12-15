import sys
import re
import collections


def distance(ax, ay, bx, by):
    return abs(ax - bx) + abs(ay - by)


def merge_intervals(intervals):
    intervals.sort()
    result = list()
    prev_start, prev_end = intervals[0]
    for curr_start, curr_end in intervals[1:]:
        if curr_start <= prev_end + 1:
            prev_end = max(prev_end, curr_end)
        else:
            result.append((prev_start, prev_end))
            prev_start, prev_end = curr_start, curr_end
    result.append((prev_start, prev_end))
    return result


with open(sys.argv[-1]) as file:
    puzzle_input = file.read().split("\n")
known_beacons = collections.defaultdict(set)
ranges = collections.defaultdict(list)

for line in puzzle_input:
    sx, sy, bx, by = map(int, re.findall(r"(-?\d+)", line))
    known_beacons[by].add(bx)
    full_radius = distance(sx, sy, bx, by)
    for y in range(sy - full_radius, sy + full_radius):
        radius = abs(full_radius - abs(sy - y))
        ranges[y].append((sx - radius, sx + radius))

combined_ranges = dict()
for y in ranges:
    combined_ranges[y] = merge_intervals(ranges[y])

query_y = 10 if sys.argv[-1] == "sample.txt" else 2_000_000
count = 0
for s, e in combined_ranges[query_y]:
    count += e - s + 1
    count -= len(known_beacons[query_y])
print("Part 1:", count)

max_y = 20 if sys.argv[-1] == "sample.txt" else 4_000_000
for y in range(0, max_y):
    if len(combined_ranges[y]) > 1:
        print(combined_ranges[y])
        x = combined_ranges[y][0][1] + 1
        print(x, y)
        print("Part 2:", 4_000_000 * x + y)
        break
