import collections
import sys


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def __lt__(self, other):
        return self.x < other.x or self.y < other.y

    def __str__(self):
        return f"({self.x}, {self.y})"

    def manhattan(self, other):
        return abs(self.x - other.x) + abs(self.y - other.y)


with open(sys.argv[1]) as file:
    raw_points = [map(int, line.split(", ")) for line in file.read().split("\n")]

points = [Point(x, y) for x, y in raw_points]

x_coords = list(map(lambda coord: coord.x, points))
y_coords = list(map(lambda coord: coord.y, points))

min_x = min(x_coords) - 1
max_x = max(x_coords) + 1
min_y = min(y_coords) - 1
max_y = max(y_coords) + 1


closest = [["" for x in range(max_x + 1)] for y in range(max_y + 1)]

for x in range(min_x, max_x):
    for y in range(min_y, max_y):
        curr_point = Point(x, y)
        distances = sorted((point.manhattan(curr_point), point) for point in points)
        if distances[0][0] != distances[1][0]:  # not a tie
            closest[y][x] = str(distances[0][1])


counter = collections.Counter([elem for row in closest for elem in row])

# remove edge points with infinite area
for x in range(min_x, max_x + 1):
    top_edge_point = closest[min_y][x]
    bottom_edge_point = closest[max_y][x]
    updates = dict()
    counter[top_edge_point] = 0
    counter[bottom_edge_point] = 0

for y in range(min_y, max_y + 1):
    left_edge_point = closest[y][min_x]
    right_edge_point = closest[y][max_x]
    updates = dict()
    counter[left_edge_point] = 0
    counter[right_edge_point] = 0

print(counter.most_common(1)[0][1])
