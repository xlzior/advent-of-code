import sys
import networkx as nx
from networkx.algorithms.connectivity import stoer_wagner

G = nx.Graph()

lines = open(sys.argv[1]).readlines()
for line in lines:
    a, right = line.strip().split(": ")
    for b in right.split(" "):
        G.add_edge(a, b)

cut, partitions = stoer_wagner(G)
left, right = partitions
print(len(left) * len(right))
