import sys
import collections


class OceanFloor:
    def __init__(self):
        self.counter = collections.Counter()

    def process_line(self, raw_string, are_diagonals_counted=False):
        [start, end] = raw_string.split(" -> ")
        [x1, y1] = list(map(int, start.split(",")))
        [x2, y2] = list(map(int, end.split(",")))

        if x1 > x2:  # make sure x1 <= x2
            x1, x2, y1, y2 = x2, x1, y2, y1

        if x1 == x2 or y1 == y2:  # vertical or horizontal lines
            if y1 > y2:  # make sure y1 <= y2
                y1, y2 = y2, y1
            self.counter.update((x, y) for x in range(x1, x2 + 1) for y in range(y1, y2 + 1))
        elif are_diagonals_counted:
            if (x1 - x2) * (y1 - y2) > 0:  # diagonal down \
                self.counter.update((x1 + i, y1 + i) for i in range(0, x2 - x1 + 1))
            else:  # diagonal up /
                self.counter.update((x1 + i, y1 - i) for i in range(0, x2 - x1 + 1))

    def parse(self, lines, are_diagonals_counted=False):
        [self.process_line(line, are_diagonals_counted) for line in lines]
        result = len(list(filter(lambda value: value > 1, self.counter.values())))
        print(result)


with open(sys.argv[1]) as file:
    raw_lines = file.readlines()

are_diagonals_counted = len(sys.argv) > 2 and sys.argv[2] == "5-2"
OceanFloor().parse(raw_lines, are_diagonals_counted)
