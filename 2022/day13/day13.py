import sys
import json
import functools


def compare(a, b):
    if isinstance(a, int) and isinstance(b, int):
        return a - b
    elif isinstance(a, list) and isinstance(b, list):
        if len(a) * len(b) == 0:
            return len(a) - len(b)

        a0, *a_rest = a
        b0, *b_rest = b
        cmp0 = compare(a0, b0)
        return compare(a_rest, b_rest) if cmp0 == 0 else cmp0
    elif isinstance(a, list):
        return compare(a, [b])
    else:
        return compare([a], b)


def less_than(a, b):
    return compare(a, b) < 0


assert less_than([1, 1, 3, 1, 1], [1, 1, 5, 1, 1]) == True
assert less_than([[1], [2, 3, 4]], [[1], 4]) == True
assert less_than([9], [[8, 7, 6]]) == False
assert less_than([[4, 4], 4, 4], [[4, 4], 4, 4, 4]) == True
assert less_than([7, 7, 7, 7], [7, 7, 7]) == False
assert less_than([], [3]) == True
assert less_than([[[]]], [[]]) == False
assert (
    less_than([1, [2, [3, [4, [5, 6, 7]]]], 8, 9], [1, [2, [3, [4, [5, 6, 0]]]], 8, 9])
    == False
)

puzzle_input = open(sys.argv[-1]).read().split("\n")
puzzle_input = filter(bool, puzzle_input)
packets = list(map(json.loads, puzzle_input))
pairs = [packets[i : i + 2] for i in range(0, len(packets), 2)]
sum_of_indices = 0
for i, pair in enumerate(pairs, 1):
    first, second = pair
    if less_than(first, second):
        sum_of_indices += i

print("Part 1:", sum_of_indices)

divider_1 = [[2]]
divider_2 = [[6]]
packets.extend([divider_1, divider_2])
packets.sort(key=functools.cmp_to_key(compare))

print("Part 2:", (packets.index(divider_1) + 1) * (packets.index(divider_2) + 1))
