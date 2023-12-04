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
        self.cycle_offset = -1
        self.cycle_length = -1

    def __str__(self):
        return f"pos={self.location}, vel={self.velocity}"

    def apply_gravity(self, other):
        x_accel = cmp(self.location.x, other.location.x)
        y_accel = cmp(self.location.y, other.location.y)
        z_accel = cmp(self.location.z, other.location.z)
        self.velocity += Point(x_accel, y_accel, z_accel)

    def apply_velocity(self):
        self.location += self.velocity
        if self.cycle_length < 0:
            if self.__str__() in self.history:
                self.cycle_offset = self.history.index(self.__str__())
                self.cycle_length = len(self.history) - self.cycle_offset
            else:
                self.history.append(self.__str__())
        else:
            assert self.__str__() in self.history

    def energy(self):
        return self.location.absolute_sum() * self.velocity.absolute_sum()


moons = list()

puzzle_input = open(sys.argv[1]).read().strip().split("\n")

for line in puzzle_input:
    x, y, z = map(int, re.findall(r"(-?\d+)", line))
    moons.append(Moon(Point(x, y, z)))

count = 0
while any(map(lambda moon: moon.cycle_length < 0, moons)):
    for moon1, moon2 in combinations(moons, 2):
        moon1.apply_gravity(moon2)
        moon2.apply_gravity(moon1)

    for moon in moons:
        moon.apply_velocity()

    count += 1
    if count == 1000:
        print("Part 1:", sum(map(lambda moon: moon.energy(), moons)))

for moon in moons:
    print(f"{moon.cycle_offset} + steps * {moon.cycle_length}")
