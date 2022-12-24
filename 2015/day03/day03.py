puzzle_input = open("puzzle.txt").read()
directions = {
    "^": complex(-1, 0),
    "v": complex(1, 0),
    "<": complex(0, -1),
    ">": complex(0, 1),
}

santa_1 = complex(0, 0)
part_1 = {santa_1}

santa_2 = [complex(0, 0), complex(0, 0)]
part_2 = set(santa_2)

for i, char in enumerate(puzzle_input):
    santa_1 += directions[char]
    part_1.add(santa_1)
    santa_2[i % 2] += directions[char]
    part_2.add(santa_2[i % 2])

print("Part 1:", len(part_1))
print("Part 2:", len(part_2))
