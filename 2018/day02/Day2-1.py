import collections
import sys

with open(sys.argv[1]) as file:
    puzzle_input = [line.strip() for line in file.readlines()]

exactly_two_count = 0
exactly_three_count = 0

for box_id in puzzle_input:
    counter = collections.Counter(box_id)
    has_exactly_two = False
    has_exactly_three = False
    for letter in counter:
        if counter[letter] == 2:
            has_exactly_two = True
        elif counter[letter] == 3:
            has_exactly_three = True

    exactly_two_count += 1 if has_exactly_two else 0
    exactly_three_count += 1 if has_exactly_three else 0

print(f"{exactly_two_count} * {exactly_three_count} = {exactly_two_count * exactly_three_count}")