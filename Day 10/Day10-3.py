import sys

open_brackets = ["(", "[", "{", "<"]
corresponding = {")": "(", "]": "[", "}": "{", ">": "<"}
incorrect = {")": 3, "]": 57, "}": 1197, ">": 25137}
incomplete = {"(": 1, "[": 2, "{": 3, "<": 4}

def get_syntax_error(line):
    stack = list()
    for bracket in line:
        if bracket in open_brackets:
            stack.append(bracket)
        elif stack[-1] == corresponding[bracket]:
            stack.pop()
        else:
            return "incorrect", bracket

    return "incomplete", stack

def get_incorrect_score(bracket):
    return incorrect[bracket]

def get_incomplete_score(remaining_brackets):
    result = 0
    while len(remaining_brackets) > 0:
        bracket = remaining_brackets.pop()
        result = result * 5 + incomplete[bracket]
    return result


puzzle_input = open(sys.argv[1]).read().split("\n")
incorrect_score = 0
incomplete_scores = list()

for line in puzzle_input:
    error_type, error_info = get_syntax_error(line)
    if error_type == "incorrect":
        incorrect_score += get_incorrect_score(error_info)
    elif error_type == "incomplete":
        incomplete_scores.append(get_incomplete_score(error_info))

print("Part 1:", incorrect_score)
print("Part 2:", sorted(incomplete_scores)[len(incomplete_scores) // 2])
