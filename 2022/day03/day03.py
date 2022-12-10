import sys


def get_shared(chunks):
    sets = [set(s) for s in chunks]
    return get_priority(sets[0].intersection(*sets[1:]).pop())


get_priority = lambda letter: ord(letter) - (96 if str.islower(letter) else 38)
puzzle_input = open(sys.argv[1]).read().strip().split("\n")

print(
    "Part 1:",
    sum(
        get_shared([line[: len(line) // 2], line[len(line) // 2 :]])
        for line in puzzle_input
    ),
)

print(
    "Part 2:",
    sum(get_shared(puzzle_input[i : i + 3]) for i in range(0, len(puzzle_input), 3)),
)
