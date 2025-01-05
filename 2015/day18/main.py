import sys

grid1 = [[c == "#" for c in row] for row in open(sys.argv[1]).read().split("\n")]
grid2 = [[c == "#" for c in row] for row in open(sys.argv[1]).read().split("\n")]


def get_cell(grid, r, c):
    if 0 <= r < len(grid) and 0 <= c < len(grid[0]):
        return grid[r][c]

    return False


def count_neighbours(grid, r, c):
    alive = 0
    for dr in [-1, 0, 1]:
        for dc in [-1, 0, 1]:
            if dr == dc == 0:
                continue
            if get_cell(grid, r + dr, c + dc):
                alive += 1
    return alive


def step(grid, stuck):
    next_state = [[False for _ in row] for row in grid]
    for r in range(len(grid)):
        for c in range(len(grid[0])):
            if (r, c) in stuck:
                next_state[r][c] = True
                continue
            n = count_neighbours(grid, r, c)
            if grid[r][c]:  # on
                next_state[r][c] = n == 2 or n == 3
            else:  # off
                next_state[r][c] = n == 3
    return next_state


empty = set()
stuck = {
    (0, 0),
    (0, len(grid1[0]) - 1),
    (len(grid1) - 1, 0),
    (len(grid1) - 1, len(grid1[0]) - 1),
}
for r, c in stuck:
    grid2[r][c] = True

for i in range(100):
    grid1 = step(grid1, empty)
    grid2 = step(grid2, stuck)

print("Part 1:", sum(sum(c for c in row) for row in grid1))
print("Part 2:", sum(sum(c for c in row) for row in grid2))
