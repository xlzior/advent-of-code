import sys


class Point:
    def __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z

    def distance_to(self, point):
        return abs(self.x - point.x) + abs(self.y - point.y) + abs(self.z - point.z)

    def __str__(self):
        return f"({self.x}, {self.y}, {self.z})"


class Nanobot:
    def __init__(self, location: Point, radius):
        self.location = location
        self.radius = radius

    def __lt__(self, other):
        return self.radius < other.radius

    def __str__(self):
        return f"{self.location}, {self.radius}"

    def distance_to(self, other):
        return self.location.distance_to(other.location)


with open(sys.argv[1]) as file:
    puzzle_input = [line.strip() for line in file.readlines()]

bots = list()

for bot_info in puzzle_input:
    raw_pos, raw_radius = bot_info.split(", r=")
    [x, y, z] = list(map(int, raw_pos.strip("pos=<").strip(">").split(",")))
    radius = int(raw_radius)
    bots.append(Nanobot(Point(x, y, z), radius))

bots.sort()
strongest_bot = bots[-1]
count = 0
for bot in bots:
    if bot.distance_to(strongest_bot) <= strongest_bot.radius:
        count += 1

print(count)
