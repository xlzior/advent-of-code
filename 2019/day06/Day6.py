import collections
import sys

puzzle_input = open(sys.argv[1]).read().strip().split("\n")

# create adjacency list
neighbours = collections.defaultdict(list)
for line in puzzle_input:
    left, right = line.split(")")
    neighbours[left].append(right)

# find the object that orbits nothing
objects = set(neighbours.keys())
orbiters = set(item for sublist in neighbours.values() for item in sublist)
root = (objects - orbiters).pop()

# starting from root node, expand outwards to find the distance to root
distance_to_root = dict()
distance_to_root[root] = 0
parent_of = dict()
nodes = [root]
while len(nodes) > 0:
    curr = nodes.pop()
    for neighbour in neighbours[curr]:
        distance_to_root[neighbour] = distance_to_root[curr] + 1
        nodes.append(neighbour)
        parent_of[neighbour] = curr

print("Part 1:", sum(distance_to_root.values()))

# compile all my ancestors
YOU = "YOU"
my_ancestors = set()
curr = YOU
while curr in parent_of:
    my_ancestors.add(parent_of[curr])
    curr = parent_of[curr]

# traverse Santa's ancestors and find the youngest common ancestor between me and santa
SAN = "SAN"
youngest_common_ancestor = SAN
while youngest_common_ancestor not in my_ancestors:
    youngest_common_ancestor = parent_of[youngest_common_ancestor]

yca_distance = distance_to_root[youngest_common_ancestor]
my_distance = distance_to_root[YOU]
santa_distance = distance_to_root[SAN]

print("Part 2:", (my_distance - yca_distance - 1) + (santa_distance - yca_distance - 1))
