import math

class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def __sub__(self, other):
        return Point(self.x - other.x, self.y - other.y)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __hash__(self):
        return hash((self.x, self.y))

    def __str__(self):
        return f"({self.x}, {self.y})"

    def angle(self):
        if self.x == 0:                          # edge cases: straight up and straight down
            return 0 if self.y < 0 else math.pi
        raw_angle = math.atan(-self.y / self.x)  # -self.y in order to account for y increasing downwards
        correction = math.pi / 2 if self.x > 0 else 3 * math.pi / 2  # convert tangent info into 360 degrees rotation
        return correction - raw_angle

    def manhattan(self, other):
        return abs(self.x - other.x) + abs(self.y - other.y)

    def euclidean(self, other):
        return math.sqrt(math.pow(self.x - other.x, 2) +
                         math.pow(self.y - other.y, 2))


deltas = {
    "UP": Point(0, -1),
    "DOWN": Point(0, 1),
    "LEFT": Point(-1, 0),
    "RIGHT": Point(1, 0)
}
