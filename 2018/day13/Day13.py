import sys


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):  # for adding a delta to a point
        return Point(self.x + other.x, self.y + other.y)

    def __eq__(self, other):  # for checking that two carts collided
        return self.x == other.x and self.y == other.y

    def __lt__(self, other):  # for sorting
        return self.y < other.y if self.y != other.y else self.x < other.x

    def __str__(self):  # for printing output
        return f"{self.x},{self.y}"


class Cart:
    deltas = {">": Point(1, 0), "<": Point(-1, 0), "v": Point(0, 1), "^": Point(0, -1)}

    def __init__(self, location, direction, manager):
        self.location = location
        self.direction = direction
        self.manager = manager  # I'd like to speak to your manager; for get_cart_at and get_track_at
        self.turn_index = 0
        self.crashed = False

    def __lt__(self, other):  # for sorting
        return self.location.__lt__(other.location)

    def get_intersection_direction(self):
        intersection_type = ["left", "straight", "right"][self.turn_index % 3]
        self.turn_index += 1
        if intersection_type == "left":
            return {">": "^", "<": "v", "v": ">", "^": "<"}[self.direction]
        elif intersection_type == "straight":
            return self.direction
        elif intersection_type == "right":
            return {">": "v", "<": "^", "v": "<", "^": ">"}[self.direction]

    def tick(self):
        if self.crashed:
            return

        next_location = self.location + self.deltas[self.direction]

        # check for collision
        other_cart = self.manager.get_cart_at(next_location)
        if other_cart:
            print(f"(Part 1) Crash site: {next_location}")
            self.crash()
            other_cart.crash()
            return

        # update direction and location accordingly
        next_spot_item = self.manager.get_track_at(next_location)
        if next_spot_item == "\\":
            self.direction = {">": "v", "<": "^", "^": "<", "v": ">"}[self.direction]
        elif next_spot_item == "/":
            self.direction = {">": "^", "<": "v", "^": ">", "v": "<"}[self.direction]
        elif next_spot_item == "+":
            self.direction = self.get_intersection_direction()
        self.location = next_location

    def crash(self):
        self.crashed = True
        self.location = Point(-1, -1)  # move off the map so that print_map is accurate


class CartManager:
    def __init__(self, puzzle_input):
        self.tracks = list(map(self.remove_carts, puzzle_input))
        self.carts = [Cart(Point(x, y), puzzle_input[y][x], self)
                      for x in range(width) for y in range(height)
                      if puzzle_input[y][x] in "><v^"]

    def remove_carts(self, row):
        return row.replace(">", "-").replace("<", "-").replace("v", "|").replace("^", "|")

    def get_track_at(self, location):
        return self.tracks[location.y][location.x]

    def get_cart_at(self, location):
        for cart in filter(lambda x: x.location == location, self.carts):
            return cart

    def get_item_at(self, location):
        cart = self.get_cart_at(location)
        return cart.direction if cart else self.get_track_at(location)

    def print_map(self):
        [print("".join(self.get_item_at(Point(x, y)) for x in range(width))) for y in range(height)]

    def run(self):
        done = False
        while not done:
            [cart.tick() for cart in sorted(self.carts)]

            not_crashed = list(filter(lambda cart: not cart.crashed, self.carts))
            if len(not_crashed) == 1:
                print("(Part 2) Final cart:", str(not_crashed[0].location))
                done = True


with open(sys.argv[1]) as file:
    puzzle_input = [line.replace("\n", "") for line in file.readlines()]
    height = len(puzzle_input)
    width = max(map(len, puzzle_input))
    puzzle_input = list(map(lambda row: row.ljust(width), puzzle_input))

CartManager(puzzle_input).run()
