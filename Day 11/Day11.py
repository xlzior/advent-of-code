import queue
import sys
import time


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def is_within_bounds(self):
        return 0 <= self.x < 10 and 0 <= self.y < 10


class OctopusManager:
    deltas = [
        Point(-1, -1), Point(0, -1), Point(1, -1),
        Point(-1,  0),               Point(1,  0),
        Point(-1,  1), Point(0,  1), Point(1,  1)
    ]

    def __init__(self, initial_energy_levels):
        self.energy_levels = initial_energy_levels
        self.flash_counter = 0
        self.height = len(initial_energy_levels)
        self.width = len(initial_energy_levels[0])

    def get_octopus(self, point):
        return self.energy_levels[point.y][point.x]

    def set_octopus(self, point, value):
        self.energy_levels[point.y][point.x] = value
        return self.energy_levels[point.y][point.x]

    def increment_octopus(self, point):
        return self.set_octopus(point, self.get_octopus(point) + 1)

    def step(self):
        gonna_flash = queue.Queue()
        flashed = set()

        # increment everybody once
        for y in range(self.height):
            for x in range(self.width):
                curr = Point(x, y)
                self.increment_octopus(curr)
                if self.get_octopus(curr) > 9:
                    gonna_flash.put(curr)

        # start handling flashes
        while not gonna_flash.empty():
            curr = gonna_flash.get()
            flashed.add(curr)
            for delta in self.deltas:
                neighbour = curr + delta
                if not neighbour.is_within_bounds():
                    continue
                neighbour_new_value = self.increment_octopus(neighbour)
                if neighbour not in flashed and neighbour_new_value == 10:  # only consider newly flashing neighbours
                    gonna_flash.put(neighbour)

        # reset all flashed octopi to 0
        for cell in flashed:
            self.set_octopus(cell, 0)

        self.flash_counter += len(flashed)  # part 1

        if len(flashed) == self.height * self.width:  # part 2
            return True

    def draw(self, last=False):
        if not last:
            print()  # so the cursor doesn't interfere with the animation
        for y in range(self.height):
            row = list()
            for x in range(self.width):
                num = self.energy_levels[y][x]
                row.append(str(num) if num > 0 else "\u001b[43;1m0\u001b[0m")
            print("".join(row))
        if not last:
            print(f"\u001b[{self.height + 2}A\u001b[{self.width}D")
            sys.stdout.flush()
            time.sleep(0.5)

    def run(self):
        i = 1
        while True:
            if self.step():
                print("Part 2:", i)
                break
            if i == 100:
                print("Part 1:", self.flash_counter)
            i += 1
            if i > 230:  # animate the ending only
                self.draw()
        self.draw(True)


with open(sys.argv[1]) as file:
    puzzle_input = [[int(char) for char in line.strip()] for line in file.readlines()]

OctopusManager(puzzle_input).run()
