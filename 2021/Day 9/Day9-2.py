import functools
import queue
import sys


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):  # to simplify adding two points together
        return Point(self.x + other.x, self.y + other.y)

    def __hash__(self):  # to add to set, needs to be hashable
        return hash(f"{self.x} {self.y}")

    def __eq__(self, other):  # to accurately determine x in set
        return self.x == other.x and self.y == other.y


deltas = [
    Point(0, -1),
    Point(0, 1),
    Point(-1, 0),
    Point(1, 0)
]


def get_height(point):  # handle out of bounds
    if 0 <= point.y < len(lines) and 0 <= point.x < len(lines[0]):
        return int(lines[point.y][point.x])
    return 10  # draw a "frame" of 10s around the grid


def find_low_points():
    low_points = list()
    # total_risk = 0
    for y in range(len(lines)):
        for x in range(len(lines[0])):
            point = Point(x, y)
            is_minimum = True
            curr_height = get_height(point)
            for delta in deltas:
                if curr_height >= get_height(point + delta):
                    is_minimum = False
                    break
            if is_minimum:
                low_points.append(Point(x, y))
                # total_risk += curr_height + 1
    return low_points


def bfs(source):
    basin = set()
    nodes_to_explore = queue.Queue()
    basin.add(source)
    nodes_to_explore.put(source)
    while not nodes_to_explore.empty():
        point = nodes_to_explore.get()
        for delta in deltas:
            neighbour = point + delta
            if neighbour not in basin and get_height(neighbour) < 9:
                basin.add(neighbour)
                nodes_to_explore.put(neighbour)
    return len(basin)


def product(lst):
    return functools.reduce(lambda x, y: x * y, lst)


with open(sys.argv[1]) as file:
    lines = list(map(lambda x: x.strip(), file.readlines()))

basin_sizes = sorted(bfs(low_point) for low_point in find_low_points())
print("top 3 basins:", basin_sizes[-3:])
print("product:", product(basin_sizes[-3:]))
