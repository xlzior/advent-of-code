puzzle_input = open("puzzle.txt").read()
print("Part 1:", puzzle_input.count("(") - puzzle_input.count(")"))

floor = 0
for i, c in enumerate(puzzle_input, 1):
    floor += 1 if c == "(" else -1
    if floor < 0:
        print("Part 2:", i)
        break
