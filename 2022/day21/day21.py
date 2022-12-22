import sys
from cpmpy import *

OPERATIONS = {
    "+": lambda a, b: a + b,
    "-": lambda a, b: a - b,
    "*": lambda a, b: a * b,
    "/": lambda a, b: a / b,
}

INVERSE = {"+": "-", "-": "+", "*": "/", "/": "*"}


def parse(line):
    key, *value = line.replace(":", "").split(" ")
    return (key, value)


def get(key):
    if isinstance(monkeys[key], int):
        return monkeys[key]

    if len(monkeys[key]) == 1:
        value = int(monkeys[key][0])
    else:
        a, op, b = monkeys[key]
        if get(a) == -1 or get(b) == -1:
            return -1
        value = int(OPERATIONS[op](get(a), get(b)))
    monkeys[key] = value
    return value


puzzle_input = open(sys.argv[-1]).read().split("\n")
monkeys = dict(map(parse, puzzle_input))
print("Part 1:", int(get("root")))

monkeys = dict(map(parse, puzzle_input))
monkeys["humn"] = -1

a, _, b = monkeys["root"]
a_val, b_val = get(a), get(b)
if a_val == -1:
    curr, value = a, b_val
else:
    curr, value = b, a_val

while curr != "humn":
    a, op, b = monkeys[curr]
    a_val, b_val = get(a), get(b)
    if a_val == -1:  # a contains humn, make a the subject
        curr = a
        value = OPERATIONS[INVERSE[op]](value, b_val)
    else:  # b contains humn, make b the subject
        curr = b
        value = {
            "+": value - a_val,
            "-": a_val - value,
            "*": value / a_val,
            "/": a_val / value,
        }[op]

print("Part 2:", int(value))
