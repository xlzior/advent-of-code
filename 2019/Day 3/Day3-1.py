import sys

class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def __str__(self):
        return f"({self.x}, {self.y})"

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __hash__(self):
        return hash((self.x, self.y))

    def manhattan_distance(self, other):
        return abs(self.x - other.x) + abs(self.y - other.y)


def manhattan_distance_to_central(point):
    return point.manhattan_distance(central_port)


def parse_path(path):
    return deltas[path[0]], int(path[1:])


deltas = {
    "U": Point(0, 1),
    "D": Point(0, -1),
    "L": Point(-1, 0),
    "R": Point(1, 0)
}

puzzle_input = open(sys.argv[1]).read().split("\n")
points = [set(), set()]
central_port = Point(0, 0)

for i in range(2):
    curr = central_port
    for path in puzzle_input[i].split(","):
        delta, distance = parse_path(path)
        for _ in range(distance):
            curr += delta
            points[i].add(curr)

crossing_points = points[0].intersection(points[1])

print(min(map(manhattan_distance_to_central, crossing_points)))
