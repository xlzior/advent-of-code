import sys, collections

directions = {
    "^": complex(-1, 0),
    "v": complex(1, 0),
    "<": complex(0, -1),
    ">": complex(0, 1),
    "": complex(0, 0),
}


def is_within_bounds(a: complex):
    return a == way_in or a == way_out or 0 <= a.real < h and 0 <= a.imag < w


def wraparound(a: complex):
    r, c = a.real, a.imag
    return complex(r % h, c % w)


def print_blizzard(blizzard):
    for r in range(h):
        for c in range(w):
            x = complex(r, c)
            if x in blizzard:
                if len(blizzard[x]) > 1:
                    print(len(blizzard[x]), end="")
                else:
                    print(blizzard[x][0], end="")
            else:
                print(".", end="")
        print()


def get_next_blizzard(blizzard):
    next_blizzards = collections.defaultdict(list)
    for k, vs in blizzard.items():
        for v in vs:
            next_blizzards[wraparound(k + directions[v])].append(v)
    return next_blizzards


def travel(start, end):
    global blizzard
    curr_minute = [start]
    time = 0
    while True:
        next_minute = set()
        for curr in curr_minute:
            if curr == end:
                return time
            for d in directions.values():
                candidate = curr + d
                if is_within_bounds(candidate) and candidate not in blizzard:
                    next_minute.add(candidate)
        time += 1
        blizzard = get_next_blizzard(blizzard)
        curr_minute = next_minute


grid = open(sys.argv[-1]).read().split("\n")
grid = [row[1:-1] for row in grid[1:-1]]
h, w = len(grid), len(grid[0])
blizzard = dict(
    (complex(r, c), [grid[r][c]])
    for r in range(h)
    for c in range(w)
    if grid[r][c] not in {"#", "."}
)
blizzard = get_next_blizzard(blizzard)

way_in = complex(-1, 0)
way_out = complex(h, w - 1)

go = travel(way_in, way_out)
print("Part 1:", go)

back = travel(way_out, way_in)
go_again = travel(way_in, way_out)
print("Part 2:", go + back + go_again)
