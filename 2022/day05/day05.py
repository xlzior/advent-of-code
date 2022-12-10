import sys
import re

initial_config, instructions = map(
    lambda x: x.split("\n"), open(sys.argv[1]).read().split("\n\n")
)
num_stacks = int(initial_config[-1].strip(" ").rsplit(" ", 1)[-1])
max_height = len(initial_config) - 1
part_1_stacks = [list() for _ in range(num_stacks)]
part_2_stacks = [list() for _ in range(num_stacks)]

for i in range(num_stacks):
    index = 4 * i + 1
    for j in range(max_height):
        char = initial_config[max_height - j - 1][index]
        if char != " ":
            part_1_stacks[i].append(char)
            part_2_stacks[i].append(char)

for i in instructions:
    num, from_stack, to_stack = map(
        int, re.match(r"move (\d+) from (\d+) to (\d+)", i).groups()
    )
    for j in range(num):
        part_1_stacks[to_stack - 1].append(part_1_stacks[from_stack - 1].pop())

    part_2_stacks[to_stack - 1].extend(part_2_stacks[from_stack - 1][-num:])
    [part_2_stacks[from_stack - 1].pop() for _ in range(num)]

print("Part 1:", "".join(stack[-1] for stack in part_1_stacks))
print("Part 2:", "".join(stack[-1] for stack in part_2_stacks))
