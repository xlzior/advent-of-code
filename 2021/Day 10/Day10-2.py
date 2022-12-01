import sys

open_brackets = ["(", "[", "{", "<"]
corresponding = {")": "(", "]": "[", "}": "{", ">": "<"}
points = {"(": 1, "[": 2, "{": 3, "<": 4}


def count_incomplete(line):
    stack = list()
    for bracket in line:
        if bracket in open_brackets:
            stack.append(bracket)
        elif stack[-1] == corresponding[bracket]:
            stack.pop()
        else:
            return False

    result = 0
    while len(stack) > 0:
        bracket = stack.pop()
        result = result * 5 + points[bracket]
    return result


with open(sys.argv[1]) as file:
    lines = file.readlines()

all_counts = [count_incomplete(line.strip()) for line in lines]
incomplete_counts = list(filter(bool, all_counts))
print(sorted(incomplete_counts)[len(incomplete_counts) // 2])
