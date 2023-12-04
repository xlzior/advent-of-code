import sys

# looping method
# for part 2, there are many cases of collapsing e.g. abcdefgGFEDCBA which would collapse to nothing
# this would take 7 iterations via this method which takes too long


def can_react(first: str, second: str):
    return first.lower() == second.lower() and first != second


def react_once(chain):
    chain = chain + " "
    next_iteration = list()

    i = 0
    while i < len(chain) - 1:
        left = chain[i]
        right = chain[i + 1]
        if can_react(left, right):
            i += 1
        else:
            next_iteration.append(left)
        i += 1

    return "".join(next_iteration)


def react_completely(chain):
    prev_length = None
    while len(chain) != prev_length:
        prev_length = len(chain)
        chain = react_once(chain)

    return chain


with open(sys.argv[1]) as file:
    puzzle_input = file.read().strip()

part_1_chain = react_completely(puzzle_input)
print("Part 1:", len(part_1_chain))

letters = set(char.lower() for char in puzzle_input)
shortest = 100000
for char in letters:
    without_char = puzzle_input.replace(char, "").replace(char.upper(), "")
    part_2_chain = react_completely(without_char)
    print(len(part_2_chain))
    shortest = min(shortest, len(part_2_chain))

print("Part 2:", shortest)
