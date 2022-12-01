import sys

# With help from: https://www.reddit.com/r/adventofcode/comments/rnejv5/comment/hps5hgw/

puzzle_input = open(sys.argv[1]).read().split("\n")
sections = [puzzle_input[i:i+18] for i in range(0, len(puzzle_input), 18)]  # input comprises 14 sections of 18 lines

# generate rules which govern the relationships between specific digits
pairs = list()
stack = list()

for i in range(len(sections)):
    section = sections[i]
    if section[4] == "div z 1":  # push to stack
        stack.append((i, int(section[15].split(" ")[-1])))
    else:  # pop from stack and form related pairs of digits
        left, a = stack.pop()
        right = i
        b = int(section[5].split(" ")[-1])
        pairs.append((left, right, a + b))  # model[left] + a + b = model[right]

print(pairs)

# start forming the largest and smallest model numbers while fulfilling the rules
min_model_number = [0] * 14
max_model_number = [0] * 14

for left, right, difference in pairs:
    if difference > 0:  # model[left] + something = model[right]
        max_model_number[right] = 9
        max_model_number[left] = 9 - difference
        min_model_number[left] = 1
        min_model_number[right] = 1 + difference
    else:  # model[left] - something = model[right]
        max_model_number[left] = 9
        max_model_number[right] = 9 + difference
        min_model_number[right] = 1
        min_model_number[left] = 1 - difference

print("Part 1:", "".join(map(str, max_model_number)))
print("Part 2:", "".join(map(str, min_model_number)))
