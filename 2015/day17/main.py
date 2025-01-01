import sys


lines = open(sys.argv[1]).read().split("\n")
capacities = list(map(int, lines))
used = [False] * len(capacities)

min_containers = len(capacities)


def count_ways(max_containers, target, curr, i):
    global min_containers
    if curr == target:
        min_containers = min(min_containers, used.count(True))
        return used.count(True) <= max_containers
    if curr > target:
        return 0

    ways = 0
    for j in range(i, len(used)):
        used[j] = True
        ways += count_ways(max_containers, target, curr + capacities[j], j + 1)
        used[j] = False
    return ways


target = 25 if sys.argv[1] == "1.in" else 150
print("Part 1:", count_ways(len(used), target, 0, 0))
print("Part 2:", count_ways(min_containers, target, 0, 0))
