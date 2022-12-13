import sys
import heapq
import collections

# grid things
DIRECTIONS = [(0, 1), (0, -1), (1, 0), (-1, 0)]
grid = [[c for c in row] for row in open(sys.argv[-1]).read().split("\n")]
h, w = len(grid), len(grid[0])
is_valid_coords = lambda r, c: 0 <= r < h and 0 <= c < w

# find start and end, and replace with a and z
get_coords_of_height = lambda char: filter(
    lambda coords: grid[coords[0]][coords[1]] == char,
    [(r, c) for r in range(h) for c in range(w)],
)
sr, sc = next(get_coords_of_height("S"))
er, ec = next(get_coords_of_height("E"))
all_as = list(get_coords_of_height("a"))
grid[sr][sc] = "a"
grid[er][ec] = "z"

can_climb = lambda a, b: ord(a) - ord(b) <= 1

distance = collections.defaultdict(lambda: 999999999)
pq = [(0, er, ec)]
distance[(er, ec)] = 0

while len(pq) > 0:
    _, r, c = heapq.heappop(pq)
    for dr, dc in DIRECTIONS:
        nr, nc = r + dr, c + dc
        if is_valid_coords(nr, nc) and can_climb(grid[r][c], grid[nr][nc]):
            alt = distance[(r, c)] + 1
            if alt < distance[(nr, nc)]:
                distance[(nr, nc)] = alt
                heapq.heappush(pq, (alt, nr, nc))

print("Part 1:", distance[(sr, sc)])
print("Part 2:", min(distance[coords] for coords in all_as))
