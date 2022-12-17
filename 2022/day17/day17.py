import sys
import collections

rocks = {
    "-": [(0, 0), (1, 0), (2, 0), (3, 0)],
    "+": [(0, 1), (1, 0), (1, 1), (1, 2), (2, 1)],
    "L": [(0, 0), (1, 0), (2, 0), (2, 1), (2, 2)],
    "I": [(0, 0), (0, 1), (0, 2), (0, 3)],
    "o": [(0, 0), (0, 1), (1, 0), (1, 1)],
}
shapes = list(rocks.keys())


def get_all_cells(x, y, shape):
    return {(x + dx, y + dy) for dx, dy in rocks[shape]}


def get_blown_by_jet(x, y, shape):
    global jet_pointer
    direction = jet_pattern[jet_pointer % len(jet_pattern)]
    jet_pointer = (jet_pointer + 1) % len(jet_pattern)
    dx = -1 if direction == "<" else 1
    for nx, ny in get_all_cells(x + dx, y, shape):
        if nx == -1 or nx == 7 or (nx, ny) in stuff:
            return x
    return x + dx


with open(sys.argv[-1]) as file:
    jet_pattern = file.read().strip()
max_y = -1
stuff = {(x, -1) for x in range(7)}
jet_pointer = 0

history = collections.defaultdict(list)
ONE_TRILLION = 1_000_000_000_000

for i in range(202200):
    shape = shapes[i % 5]
    x, y = 2, max_y + 4
    x = get_blown_by_jet(x, y, shape)
    while len(get_all_cells(x, y - 1, shape).intersection(stuff)) == 0:
        y -= 1
        x = get_blown_by_jet(x, y, shape)
    history[(jet_pointer, shape)].append((i, max_y))
    if len(history[(jet_pointer, shape)]) == 3:
        a, b, c = history[(jet_pointer, shape)]
        ai, ay = a
        bi, by = b
        ci, cy = c
        if by - ay == cy - by and bi - ai == ci - bi:
            shapes_per_cycle = bi - ai
            height_per_cycle = by - ay
            if (ONE_TRILLION - ci) % shapes_per_cycle == 0:
                num_cycles_left = (ONE_TRILLION - ci) // shapes_per_cycle
                answer = num_cycles_left * height_per_cycle + max_y + 1
                print("Part 2:", answer)
                break
    all_cells = get_all_cells(x, y, shape)
    stuff.update(all_cells)
    max_y = max(max_y, max(y for (_, y) in all_cells))
    if i == 2021:
        print("Part 1:", max_y + 1)
