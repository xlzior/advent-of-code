import sys
from queue import PriorityQueue


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def __lt__(self, other):
        return self.x < other.x or self.y < other.y

    def is_within_bounds(self):
        return 0 <= self.x < width and 0 <= self.y < height


def get_weight(point):
    return int(puzzle_input[point.y][point.x])


def get_shortest_path(point):
    return shortest_paths[point.y][point.x]


def set_shortest_path(point, value):
    shortest_paths[point.y][point.x] = value


deltas = [Point(0, -1), Point(0, 1), Point(-1, 0), Point(1, 0)]

with open(sys.argv[1]) as file:
    puzzle_input = file.read().split("\n")
    height = len(puzzle_input)
    width = len(puzzle_input[0])

pq = PriorityQueue()
pq.put((0, Point(0, 0)))
end_node = Point(width - 1, height - 1)

shortest_paths = [[10000000000 for i in range(width)] for j in range(height)]

while not pq.empty():
    distance, node = pq.get()

    set_shortest_path(node, distance)
    if node == end_node:  # terminate early if I finalised the end node
        break

    for delta in deltas:
        neighbour = node + delta
        if neighbour.is_within_bounds():
            new_path = distance + get_weight(neighbour)
            if new_path < get_shortest_path(neighbour):
                set_shortest_path(neighbour, new_path)
                pq.put((new_path, neighbour))

print(shortest_paths[end_node.y][end_node.x])
