import sys
import re
from cpmpy import *

lines = open(sys.argv[1]).readlines()
hails = [tuple(map(int, re.findall("-?\d+", line))) for line in lines]

constraints = list()

space = 1_000
x, y, z = intvar(0, space, shape=3)
dx, dy, dz = intvar(-space, space, shape=3)

for hx, hy, hz, hdx, hdy, hdz in hails:
    t = intvar(0, space)
    constraints.extend(
        [
            x + t * dx == hx + t * hdx,
            y + t * dy == hy + t * hdy,
            z + t * dz == hz + t * hdz,
        ]
    )

model = Model(constraints)
model.solve()

print(x.value(), y.value(), z.value())
print(x.value() + y.value() + z.value())
