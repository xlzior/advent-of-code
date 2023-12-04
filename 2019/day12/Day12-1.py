import re
import sys
from itertools import combinations
from Point import Point

def cmp(a, b):
    if a < b:
        return 1
    elif a > b:
        return -1
    else:
        return 0

class Moon:
    def __init__(self, location: Point):
        self.location = location
        self.velocity = Point(0, 0, 0)
        self.history = [self.__str__()]

    def __str__(self):
        return f"pos={self.location}, vel={self.velocity}"

    def apply_gravity(self, other):
        x_accel = cmp(self.location.x, other.location.x)
        y_accel = cmp(self.location.y, other.location.y)
        z_accel = cmp(self.location.z, other.location.z)
        self.velocity += Point(x_accel, y_accel, z_accel)

    def apply_velocity(self):
        self.location += self.velocity

    def energy(self):
        return self.location.absolute_sum() * self.velocity.absolute_sum()


puzzle_input = open(sys.argv[1]).read().strip().split("\n")
moons = list()

for line in puzzle_input:
    x, y, z = map(int, re.findall(r"(-?\d+)", line))
    moons.append(Moon(Point(x, y, z)))

for _ in range(1000):
    for moon1, moon2 in combinations(moons, 2):
        moon1.apply_gravity(moon2)
        moon2.apply_gravity(moon1)

    for moon in moons:
        moon.apply_velocity()

print("Part 1:", sum(map(lambda moon: moon.energy(), moons)))
