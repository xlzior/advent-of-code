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


def get_combined_steps(point):
    return points[0][point] + points[1][point]


def parse_path(path):
    return deltas[path[0]], int(path[1:])


deltas = {
    "U": Point(0, 1),
    "D": Point(0, -1),
    "L": Point(-1, 0),
    "R": Point(1, 0)
}

puzzle_input = open(sys.argv[1]).read().split("\n")
points = [dict(), dict()]
central_port = Point(0, 0)

for i in range(2):
    steps_count = 1
    curr = central_port
    for path in puzzle_input[i].split(","):
        delta, distance = parse_path(path)
        for j in range(distance):
            curr += delta
            points[i][curr] = steps_count + j
        steps_count += distance

crossing_points = points[0].keys() & points[1].keys()

print(min(map(get_combined_steps, crossing_points)))
