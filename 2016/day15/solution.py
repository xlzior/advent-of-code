import sys
import re
from cpmpy import *

pattern = r"Disc #(\d+) has (\d+) positions.+position (\d+)"
lines = open(sys.argv[1]).readlines()
discs = [tuple(map(int, re.search(pattern, line).groups())) for line in lines]


def solve(discs):
    time = intvar(0, 1_000_000_000)
    Model(
        [(start + disc + time) % divisor == 0 for (disc, divisor, start) in discs]
    ).solve()
    return time.value()


print("Part 1:", solve(discs))
print("Part 2:", solve(discs + [(len(discs) + 1, 11, 0)]))
