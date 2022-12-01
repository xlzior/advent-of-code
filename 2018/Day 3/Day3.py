import sys
import re


class Rectangle:
    def __init__(self, raw_string):
        match = re.match(r"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)", raw_string)
        id, start_x, start_y, width, height = list(map(int, match.groups()))
        self.id = id
        self.start_x = start_x
        self.start_y = start_y
        self.end_x = start_x + width
        self.end_y = start_y + height

    def update_counts_in(self, num_of_claims):  # time complexity is dependent on size of each rectangle
        for x in range(self.start_x, self.end_x):
            for y in range(self.start_y, self.end_y):
                num_of_claims[y][x] += 1

    def has_competition(self):
        for x in range(self.start_x, self.end_x):
            for y in range(self.start_y, self.end_y):
                if num_of_claims[y][x] > 1:
                    return True

        return False


with open(sys.argv[1]) as file:
    puzzle_input = file.read().split("\n")

claims = [Rectangle(line) for line in puzzle_input]
max_x = max(map(lambda claim: claim.end_x, claims))
max_y = max(map(lambda claim: claim.end_y, claims))

num_of_claims = [[0 for i in range(max_x)] for j in range(max_y)]

for claim in claims:
    claim.update_counts_in(num_of_claims)

print("Part 1:", sum(1 for row in num_of_claims for cell in row if cell > 1))

for claim in claims:
    if not claim.has_competition():
        print("Part 2:", claim.id)
        break
