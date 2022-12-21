import sys
from cpmpy import *

OPERATIONS = {
    "+": lambda a, b: a + b,
    "-": lambda a, b: a - b,
    "*": lambda a, b: a * b,
    "/": lambda a, b: a / b,
}


def parse(line):
    key, *value = line.replace(":", "").split(" ")
    return (key, value)


def get(key):
    if len(monkeys[key]) == 1:
        return int(monkeys[key][0])

    a, op, b = monkeys[key]
    return OPERATIONS[op](get(a), get(b))


monkeys = dict(map(parse, open(sys.argv[-1]).read().split("\n")))

print("Part 1:", int(get("root")))


def create_variable(key):
    return (key, intvar(1, 1_000_000_000_000_000, name=key))


def create_constraint(key):
    if key == "humn":
        return

    if len(monkeys[key]) == 1:
        return vars[key] == int(monkeys[key][0])

    a, op, b = monkeys[key]
    if key == "root":
        return vars[a] == vars[b]

    return vars[key] == OPERATIONS[op](vars[a], vars[b])


vars = dict(map(create_variable, monkeys))
constraints = list(filter(bool, map(create_constraint, monkeys)))
model = Model(constraints)
model.solve()
print("Part 2:", vars["humn"].value())
