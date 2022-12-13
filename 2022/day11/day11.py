import sys
import heapq

OPERATIONS = {"+": lambda a, b: a + b, "*": lambda a, b: a * b}


class Monkey:
    def __init__(self, starting_items, op, test, if_true, if_false) -> None:
        self.starting_items = starting_items.copy()
        self.items = starting_items
        self.op = op
        self.test = test
        self.if_true = if_true
        self.if_false = if_false
        self.count = 0

    def simulate_round(self):
        for item in self.items:
            b = item if self.op[1] == "old" else int(self.op[1])
            if part == 1:
                item = OPERATIONS[self.op[0]](item, b) // 3
            else:
                item = OPERATIONS[self.op[0]](item, b) % lcm
            throw_to_monkey = self.if_true if item % self.test == 0 else self.if_false
            monkeys[throw_to_monkey].items.append(item)

        self.count += len(self.items)
        self.items.clear()

    def reset(self):
        self.items = self.starting_items
        self.count = 0


puzzle_input = open(sys.argv[-1]).read().split("\n\n")
monkeys = list()
lcm = 1

for i, monkey in enumerate(puzzle_input):
    monkey = monkey.split("\n")
    lcm *= int(monkey[3].rsplit(" ")[-1])
    monkeys.append(
        Monkey(
            starting_items=list(map(int, monkey[1].split(":")[-1].strip().split(", "))),
            op=monkey[2].rsplit(" ")[-2:],
            test=int(monkey[3].rsplit(" ")[-1]),
            if_true=int(monkey[4].rsplit(" ")[-1]),
            if_false=int(monkey[5].rsplit(" ")[-1]),
        )
    )

part = 1
for _ in range(20):
    for monkey in monkeys:
        monkey.simulate_round()
most_active = heapq.nlargest(2, [monkey.count for monkey in monkeys])
print("Part 1:", most_active[0] * most_active[1])

part = 2
[monkey.reset() for monkey in monkeys]
for _ in range(10_000):
    for monkey in monkeys:
        monkey.simulate_round()
most_active = heapq.nlargest(2, [monkey.count for monkey in monkeys])
print("Part 2:", most_active[0] * most_active[1])
