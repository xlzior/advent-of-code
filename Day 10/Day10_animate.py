import sys
import time
from termcolor import colored

# main problem-related logic
open_brackets = ["(", "[", "{", "<"]
corresponding = {")": "(", "]": "[", "}": "{", ">": "<"}
missing = {"(": ")", "[": "]", "{": "}", "<": ">"}
incorrect = {")": 3, "]": 57, "}": 1197, ">": 25137}
incomplete = {"(": 1, "[": 2, "{": 3, "<": 4}

def get_syntax_error(line):
    stack = list()
    print()
    for i in range(len(line)):
        bracket = line[i]
        if bracket in open_brackets:
            stack.append(bracket)
            draw(line[:i], line[i:])
        elif stack[-1] == corresponding[bracket]:
            stack.pop()
            draw(line[:i], line[i:])
        else:
            draw(line[:i], line[i + 1:], ("incorrect", bracket))
            return "incorrect", bracket

    draw(line, "", ("incomplete", stack))
    return "incomplete", stack

def get_incorrect_score(bracket):
    return incorrect[bracket]

def get_incomplete_score(remaining_brackets):
    remaining_brackets = remaining_brackets.copy()
    result = 0
    while len(remaining_brackets) > 0:
        bracket = remaining_brackets.pop()
        result = result * 5 + incomplete[bracket]
    return result

# color utilities
def red(raw):
    return colored(raw, 'red')

def yellow(raw):
    return colored(raw, 'yellow')

def dark_red(raw):
    return colored(raw, 'red', attrs=['dark'])

def dark_yellow(raw):
    return colored(raw, 'yellow', attrs=['dark'])

# drawing helpers
def generate_missing_brackets(remaining_brackets):
    return "".join(map(lambda bracket: missing[bracket], reversed(remaining_brackets)))

def get_error_display(error):
    error_type, error_info = error
    if error_type == "incorrect":
        return len(error_info), red(error_info), f"{error_type}   {red(get_incorrect_score(error_info))}"
    elif error_type == "incomplete":
        missing_brackets = generate_missing_brackets(error_info)
        return len(missing_brackets), yellow(missing_brackets), f"{error_type}  {yellow(get_incomplete_score(error_info))}"
    return 0, "", ""

def draw(seen, unseen, error=(None, None)):
    print()  # so the cursor doesn't interfere
    length = len(seen) + len(unseen)
    seen = colored(seen, "white")
    unseen = colored(unseen, "grey")
    error_section_length, error_section, error_display = get_error_display(error)
    length += error_section_length
    padding = ' ' * (12 + max_length - length)  # 12 is an estimate for number of missing brackets
    print(f"{seen}{error_section}{unseen}{padding}{error_display}")
    print(f"\u001b[{3}A\u001b[{9999}D")
    time.sleep(0.1)


puzzle_input = open(sys.argv[1]).read().split("\n")
incorrect_scores = list()
incomplete_scores = list()
max_length = max(map(len, puzzle_input))

# detect errors
for line in puzzle_input:
    error_type, error_info = get_syntax_error(line)
    if error_type == "incorrect":
        incorrect_scores.append(get_incorrect_score(error_info))
    elif error_type == "incomplete":
        incomplete_scores.append(get_incomplete_score(error_info))

# prepare final printout
print(f"\u001b[{2}B")  # move cursor back down

# part 1
incorrect_scores_display = ' + '.join(map(dark_red, incorrect_scores))
syntax_error_score = red(sum(incorrect_scores))
print(f"Part 1: {incorrect_scores_display} = {syntax_error_score}")

# part 2
incomplete_scores.sort()
num_incomplete = len(incomplete_scores)

# break the array into sections
first_half = ", ".join(map(str, incomplete_scores[:num_incomplete // 2]))
middle = incomplete_scores[num_incomplete // 2]
second_half = ", ".join(map(str, incomplete_scores[num_incomplete // 2 + 1:]))

# style each section
first_half = dark_yellow(first_half)
middle = yellow(middle)
second_half = dark_yellow(second_half)

print(f"Part 2: [{first_half}, {middle}, {second_half}]")
