import sys

# stack method
# in the case of collapsing e.g. abcdefgGFEDCBA,
# we check *on the spot* for more reactions, rather than waiting for another iteration
# hence, there are 7 loops of the inner while loop which happens faster and we "keep track of where we are"
# the outer for loop only needs to iterate once through the entire string


def can_react(first: str, second: str):
    return first.lower() == second.lower() and first != second


def react_completely(chain):
    stack = list()
    for char in chain:
        stack.append(char)
        while len(stack) >= 2 and can_react(stack[-2], stack[-1]):
            stack.pop()
            stack.pop()

    return stack


with open(sys.argv[1]) as file:
    puzzle_input = file.read().strip()

part_1_chain = react_completely(puzzle_input)
print("Part 1:", len(part_1_chain))

letters = set(char.lower() for char in puzzle_input)
shortest = 100000
for char in letters:
    without_char = puzzle_input.replace(char, "").replace(char.upper(), "")
    part_2_chain = react_completely(without_char)
    shortest = min(shortest, len(part_2_chain))

print("Part 2:", shortest)
