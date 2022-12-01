import collections
import sys

OPEN = "."
TREES = "|"
LUMBERYARD = "#"


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def is_within_bounds_of(self, board):
        height = len(board)
        width = len(board[0])
        return 0 <= self.x < width and 0 <= self.y < height


deltas = [
    Point(-1, -1), Point(0, -1), Point(1, -1),
    Point(-1, 0), Point(1, 0),
    Point(-1, 1), Point(0, 1), Point(1, 1)
]


def get_state(board, point):
    return board[point.y][point.x]


def next_state(current, board):
    surroundings = collections.Counter()
    for delta in deltas:
        neighbour = current + delta
        if neighbour.is_within_bounds_of(board):
            surroundings.update(get_state(board, neighbour))

    current_state = get_state(board, current)
    if current_state == OPEN:
        return TREES if surroundings[TREES] >= 3 else OPEN
    elif current_state == TREES:
        return LUMBERYARD if surroundings[LUMBERYARD] >= 3 else TREES
    elif current_state == LUMBERYARD:
        return LUMBERYARD if surroundings[LUMBERYARD] >= 1 and surroundings[TREES] >= 1 else OPEN


class Simulator:
    def __init__(self, initial):
        self.state = initial
        self.height = len(initial)
        self.width = len(initial[0])

    def run(self, n):
        for i in range(n):
            self.one_minute_forward()

        print(self.calculate_resource_value(self.state))

    def calculate_resource_value(self, board):
        counter = collections.Counter("".join(board))
        resource_value = counter[TREES] * counter[LUMBERYARD]
        return resource_value

    def one_minute_forward(self):
        self.state = ["".join(next_state(Point(x, y), self.state) for x in range(self.width)) for y in range(self.height)]

    def find_repeating_pattern(self):
        history_set = set()
        history_list = list()
        self.one_minute_forward()
        curr = hash("\n".join(self.state)), self.calculate_resource_value(self.state)

        while curr not in history_set:
            history_list.append(curr)
            history_set.add(curr)
            self.one_minute_forward()
            curr = hash("\n".join(self.state)), self.calculate_resource_value(self.state)

        self.initial_length = history_list.index(curr)
        self.cycle = history_list[self.initial_length:]
        print(f"Pattern repeats after {self.initial_length} with a cycle length of {len(self.cycle)}")

    def predict_resource_value(self, n):
        self.find_repeating_pattern()
        index = (n - self.initial_length - 1) % len(self.cycle)
        print(self.cycle[index][1])


with open(sys.argv[1]) as file:
    puzzle_input = file.read().split("\n")

# Simulator(puzzle_input).run(10)  # part 1
Simulator(puzzle_input).predict_resource_value(1000000000)  # part 2
