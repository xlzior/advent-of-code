import sys

entirely_contains = lambda a, b: a[0] <= b[0] and a[1] >= b[1]
overlaps = lambda a, b: a[0] <= b[1] and a[1] >= b[0]

puzzle_input = open(sys.argv[1]).read().strip().split("\n")
part_1_count = 0
part_2_count = 0

for line in puzzle_input:
    a, b = [list(map(int, r.split("-"))) for r in line.split(",")]
    if entirely_contains(a, b) or entirely_contains(b, a):
        part_1_count += 1
    if overlaps(a, b) or overlaps(b, a):
        part_2_count += 1

print("Part 1:", part_1_count)
print("Part 2:", part_2_count)
