import sys, re


board, instructions = open(sys.argv[-1]).read().split("\n\n")
board = board.split("\n")
h, w = len(board), max(len(row) for row in board)
board = [row.ljust(w) for row in board]
instructions = [
    int(x) if i % 2 == 0 else x
    for i, x in enumerate(re.findall(r"\d+|[A-Z]+", instructions))
]

# Part 1 wraparound behaviour

top = [999] * w
bottom = [-1] * w
left = [999] * h
right = [-1] * h

for r in range(h):
    for c in range(w):
        if board[r][c] != " ":
            top[c] = min(top[c], r)
            bottom[c] = max(bottom[c], r)
            left[r] = min(left[r], c)
            right[r] = max(right[r], c)


def wraparound_1(r, c, f):
    if f % 2 == 0:  # left or right
        if left[r] == c:
            return (r, right[r], f)
        elif right[r] == c:
            return (r, left[r], f)
    else:  # up or down
        if top[c] == r:
            return (bottom[c], c, f)
        elif bottom[c] == r:
            return (top[c], c, f)


# Part 2 wraparound behaviour

"""
my puzzle input looks like this:
 12
 3
45
6
"""


def get_section(r, c):
    if r < 50:
        return 1 if c < 100 else 2
    elif r < 100:
        return 3
    elif r < 150:
        return 4 if c < 50 else 5
    else:
        return 6


# first key is section, second key is f
wraparound_2 = {
    1: {
        2: lambda r, c: (149 - r, 0, 0),
        3: lambda r, c: (c + 100, 0, 0),
    },
    2: {
        0: lambda r, c: (149 - r, 99, 2),
        1: lambda r, c: (c - 50, 99, 2),
        3: lambda r, c: (199, c - 100, 3),
    },
    3: {
        0: lambda r, c: (49, r + 50, 3),
        2: lambda r, c: (100, r - 50, 1),
    },
    4: {
        2: lambda r, c: (149 - r, 50, 0),
        3: lambda r, c: (c + 50, 50, 0),
    },
    5: {
        0: lambda r, c: (149 - r, 149, 2),
        1: lambda r, c: (c + 100, 49, 2),
    },
    6: {
        0: lambda r, c: (149, r - 100, 3),
        1: lambda r, c: (0, c + 100, 1),
        2: lambda r, c: (0, r - 100, 1),
    },
}


def get_next(part, r, c, f):
    dr, dc = [(0, 1), (1, 0), (0, -1), (-1, 0)][f]

    # within the bounds and not blank, no wraparound behaviour
    if 0 <= r + dr < h and 0 <= c + dc < w and board[r + dr][c + dc] != " ":
        return r + dr, c + dc, f

    if part == 1:
        return wraparound_1(r, c, f)
    else:
        return wraparound_2[get_section(r, c)][f](r, c)


def run(part):
    if part == 2 and sys.argv[-1] != "puzzle.txt":
        return

    r, c, f = 0, left[0], 0

    for i in instructions:
        if isinstance(i, str):
            f = (f + (1 if i == "R" else -1)) % 4
        else:
            for _ in range(i):
                nr, nc, nf = get_next(part, r, c, f)
                if board[nr][nc] == "#":
                    break
                r, c, f = nr, nc, nf

    return 1000 * (r + 1) + 4 * (c + 1) + f


print("Part 1:", run(1))
print("Part 2:", run(2))
