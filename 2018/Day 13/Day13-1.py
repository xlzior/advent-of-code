import sys


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __lt__(self, other):
        return self.y < other.y if self.y != other.y else self.x < other.x

    def __str__(self):
        return f"{self.x},{self.y}"

    def is_within_bounds(self):
        return 0 <= self.x < width and 0 <= self.y < height


def turn_at_intersection(turn_index, current_direction):
    intersection_type = ["left", "straight", "right"][turn_index % 3]
    if intersection_type == "left":
        return {">": "^", "<": "v", "v": ">", "^": "<"}[current_direction]
    elif intersection_type == "straight":
        return current_direction
    elif intersection_type == "right":
        return {">": "v", "<": "^", "v": "<", "^": ">"}[current_direction]


class Cart:
    def __init__(self, location, direction):
        self.location = location
        self.direction = direction
        self.turn_index = 0

    def get_intersection_direction(self):
        new_direction = turn_at_intersection(self.turn_index, self.direction)
        self.turn_index += 1
        return new_direction

    def __lt__(self, other):
        return self.location.__lt__(other.location)

    def __str__(self):
        return f"{str(self.location)}: {self.direction}"


deltas = {">": Point(1, 0), "<": Point(-1, 0), "v": Point(0, 1), "^": Point(0, -1)}

with open(sys.argv[1]) as file:
    puzzle_input = [line.replace("\n", "") for line in file.readlines()]

height = len(puzzle_input)
width = max(map(len, puzzle_input))
puzzle_input = list(map(lambda row: row.ljust(width), puzzle_input))


def remove_carts(row):
    return row.replace(">", "-").replace("<", "-").replace("v", "|").replace("^", "|")


carts = [Cart(Point(x, y), puzzle_input[y][x]) for x in range(width) for y in range(height) if puzzle_input[y][x] in "><v^"]
tracks = [remove_carts(row) for row in puzzle_input]


def get_track_item(point):
    return tracks[point.y][point.x] if point.is_within_bounds() else " "


def has_cart(location):
    carts_at_location = list(filter(lambda cart: cart.location == location, carts))
    if len(carts_at_location) > 0:
        return carts_at_location


def tick():
    carts.sort()
    for cart in carts:
        cart_direction = cart.direction
        delta = deltas[cart_direction]
        next_location = cart.location + delta
        if has_cart(next_location):
            return next_location

        next_spot_item = get_track_item(next_location)
        assert next_spot_item != " "
        new_direction = cart_direction
        if next_spot_item == "\\":
            new_direction = {">": "v", "<": "^", "^": "<", "v": ">"}[cart_direction]
        elif next_spot_item == "/":
            new_direction = {">": "^", "<": "v", "^": ">", "v": "<"}[cart_direction]
        elif next_spot_item == "+":
            new_direction = cart.get_intersection_direction()

        cart.direction = new_direction
        cart.location = next_location


def print_map():
    for y in range(height):
        row = ""
        for x in range(width):
            point = Point(x, y)
            cart = has_cart(point)
            if cart:
                row += cart.direction
            else:
                row += get_track_item(point)
        print(row)


while True:
    crash_point = tick()
    if crash_point:
        print(crash_point)
        break
