import collections
import sys


class Day12:
    def __init__(self, neighbours):
        self.count = 0
        self.neighbours = neighbours

    def visit(self, cave: str, history: set, quota_used: bool):
        if cave == "end":
            self.count += 1
            return

        for neighbour in neighbours[cave]:
            if cave == "start" and len(history) > 1:
                return
            new_history = history.copy()
            new_history.add(neighbour)
            if neighbour.isupper() or neighbour not in history:
                self.visit(neighbour, new_history, quota_used)
            elif not quota_used:
                self.visit(neighbour, new_history, True)

    def run(self):
        self.visit("start", {"start"}, False)
        print(self.count)


with open(sys.argv[1]) as file:
    lines = file.read().split("\n")

neighbours = collections.defaultdict(list)
for line in lines:
    u, v = line.split("-")
    neighbours[u].append(v)
    neighbours[v].append(u)

Day12(neighbours).run()
