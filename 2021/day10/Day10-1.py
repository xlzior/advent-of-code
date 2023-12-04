import sys

open_brackets = ["(", "[", "{", "<"]
corresponding = {")": "(", "]": "[", "}": "{", ">": "<"}
points = {")": 3, "]": 57, "}": 1197, ">": 25137}


def count_corrupted(line):
    stack = list()
    for bracket in line:
        if bracket in open_brackets:
            stack.append(bracket)
        elif stack[-1] == corresponding[bracket]:
            stack.pop()
        else:
            return points[bracket]
    return 0


with open(sys.argv[1]) as file:
    lines = file.readlines()

print(sum(count_corrupted(line.strip()) for line in lines))
