import sys
from sympy import *

lines = open(sys.argv[1]).readlines()
hails = [list(map(int, line.replace("@", ",").split(","))) for line in lines]

x, y, z = symbols("x y z", integer=True)
dx, dy, dz = symbols("dx dy dz")
constraints = list()

for xi, yi, zi, dxi, dyi, dzi in hails:
    constraints.append(Eq((xi - x) * (dyi - dy), (yi - y) * (dxi - dx)))
    constraints.append(Eq((xi - x) * (dzi - dz), (zi - z) * (dxi - dx)))

solution = solve(constraints, [x, y, z, dx, dy, dz])
print(solution)
print(sum(solution[0][:3]))
