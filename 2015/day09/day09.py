import sys, re, itertools


def calculate_distance(perm):
    return sum(distances[perm[i : i + 2]] for i in range(len(perm) - 1))


puzzle_input = open(sys.argv[-1]).read().split("\n")
locations = set()
distances = dict()

for line in puzzle_input:
    start, end, distance = re.match(r"(\w+) to (\w+) = (\d+)", line).groups()
    locations.add(start)
    locations.add(end)
    distances[(start, end)] = int(distance)
    distances[(end, start)] = int(distance)

print("Part 1:", min(map(calculate_distance, itertools.permutations(locations))))
print("Part 1:", max(map(calculate_distance, itertools.permutations(locations))))
