import re


criteria = open(("1.in")).read().split("\n")
criteria = [line.split(": ") for line in criteria]
criteria = {k: int(v) for k, v in criteria}

aunts = open(("puzzle.in")).read().split("\n")

greater_than = lambda a, b: a > b
less_than = lambda a, b: a < b
equal_to = lambda a, b: a == b

comparators = {
    "cats": greater_than,
    "trees": greater_than,
    "pomeranians": less_than,
    "goldfish": less_than,
}

part1 = -1
part2 = -1

for aunt in aunts:
    id, k1, v1, k2, v2, k3, v3 = re.match(
        r"Sue (\d+): (\w+): (\d+), (\w+): (\d+), (\w+): (\d+)", aunt
    ).groups()
    assertions = [(k1, v1), (k2, v2), (k3, v3)]
    matching1 = 0
    matching2 = 0
    for k, v in assertions:
        if criteria[k] == int(v):
            matching1 += 1
        cmp = comparators.get(k, equal_to)
        if cmp(int(v), criteria[k]):
            matching2 += 1
    if matching1 == 3:
        part1 = id
    if matching2 == 3:
        part2 = id

print("Part 1:", part1)
print("Part 2:", part2)
