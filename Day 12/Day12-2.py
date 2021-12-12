import collections
import queue
import sys

with open(sys.argv[1]) as file:
    lines = file.read().split("\n")

neighbours = collections.defaultdict(list)
for line in lines:
    u, v = line.split("-")
    neighbours[u].append(v)
    neighbours[v].append(u)

minions = queue.Queue()
minions.put(['start'])
count = 0

while not minions.empty():
    minion = minions.get()
    cave = minion[-1]
    if cave == "end":
        count += 1
        continue
    for neighbour in neighbours[cave]:
        if neighbour == "start":  # can't revisit start
            continue

        small_caves = list(filter(lambda x: x.islower(), minion))
        num_small_caves_visited_twice = len(small_caves) - len(set(small_caves))
        if neighbour.isupper() or num_small_caves_visited_twice <= 1:
            minions.put(minion + [neighbour])

print(count)
