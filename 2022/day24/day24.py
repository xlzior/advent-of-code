import sys, queue, collections

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


grid = open(sys.argv[-1]).read().split("\n")
grid = [row[1:-1] for row in grid[1:-1]]
h, w = len(grid), len(grid[0])
blizzards = list()
blizzards.append(
    dict(
        (complex(r, c), [grid[r][c]])
        for r in range(h)
        for c in range(w)
        if grid[r][c] not in {"#", "."}
    )
)

way_in = complex(-1, 0)
way_out = complex(h, w - 1)
for i in range(1_000):
    next_blizzards = collections.defaultdict(list)
    for k, vs in blizzards[-1].items():
        for v in vs:
            next_blizzards[wraparound(k + directions[v])].append(v)
    blizzards.append(next_blizzards)


def travel(start_time, start, end):
    bfs = queue.Queue()
    bfs.put((start, start_time))
    seen = {(start, start_time)}
    while not bfs.empty():
        curr, time = bfs.get()
        if curr == end:
            return time
        for d in directions.values():
            candidate = (curr + d, time + 1)
            if (
                is_within_bounds(candidate[0])
                and candidate[0] not in blizzards[time + 1]
                and candidate not in seen
            ):
                bfs.put(candidate)
                seen.add(candidate)


go = travel(0, way_in, way_out)
print("Part 1:", go)

back = travel(go, way_out, way_in)
go_again = travel(back, way_in, way_out)
print("Part 2:", go_again)
