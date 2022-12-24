import re, collections

parse = lambda line: re.match(
    r"(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)", line
).groups()
puzzle_input = open("puzzle.txt").read().split("\n")
instructions = map(parse, puzzle_input)

switched_on = set()
brightness = collections.defaultdict(int)
for setting, *nums in instructions:
    ax, ay, bx, by = map(int, nums)
    for x in range(ax, bx + 1):
        for y in range(ay, by + 1):
            if setting == "turn on":
                switched_on.add((x, y))
                brightness[(x, y)] += 1
            elif setting == "turn off":
                if (x, y) in switched_on:
                    switched_on.remove((x, y))
                brightness[(x, y)] = max(brightness[(x, y)] - 1, 0)
            elif setting == "toggle":
                if (x, y) in switched_on:
                    switched_on.remove((x, y))
                else:
                    switched_on.add((x, y))
                brightness[(x, y)] += 2

print("Part 1:", len(switched_on))
print("Part 2:", sum(brightness.values()))
