import sys
import math

DIRECTIONS = {"up": (-1, 0), "down": (1, 0), "left": (0, -1), "right": (0, 1)}
DIR_TO_INDEX = {"down": 0, "right": 1, "up": 2, "left": 3}
grid = list(map(lambda s: [int(c) for c in s], open(sys.argv[1]).read().split("\n")))
h, w = len(grid), len(grid[0])
is_valid_coords = lambda r, c: 0 <= r < h and 0 <= c < w
viewing_distances = [[[0, 0, 0, 0] for _ in range(w)] for _ in range(h)]

stack = list()
for r in range(h):
    stack.append((r, 0, "right", grid[r][0]))
    stack.append((r, w - 1, "left", grid[r][w - 1]))
for c in range(w):
    stack.append((0, c, "down", grid[0][c]))
    stack.append((h - 1, c, "up", grid[h - 1][c]))

visible = set((s[0], s[1]) for s in stack)

while len(stack) > 0:
    r, c, dir, tallest_height = stack.pop()
    dr, dc = DIRECTIONS[dir]
    nr, nc = r + dr, c + dc
    if not is_valid_coords(nr, nc):
        continue
    if grid[nr][nc] > tallest_height:
        visible.add((nr, nc))
        stack.append((nr, nc, dir, grid[nr][nc]))
    else:
        stack.append((nr, nc, dir, tallest_height))

print("Part 1:", len(visible))

for dir in DIRECTIONS:
    dr, dc = DIRECTIONS[dir]
    index = DIR_TO_INDEX[dir]

    for r in range(h):
        for c in range(w):
            count = 0
            nr, nc = r + (count + 1) * dr, c + (count + 1) * dc
            while is_valid_coords(nr, nc):
                count += 1
                if grid[nr][nc] >= grid[r][c]:
                    break
                nr, nc = r + (count + 1) * dr, c + (count + 1) * dc
            viewing_distances[r][c][index] = count

print(
    "Part 2:", max(map(math.prod, [cell for row in viewing_distances for cell in row]))
)
