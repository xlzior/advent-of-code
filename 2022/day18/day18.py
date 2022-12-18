import sys

with open(sys.argv[-1]) as file:
    puzzle_input = file.read().split("\n")
lava = set(tuple(map(int, line.split(","))) for line in puzzle_input)

# determine bounding box so that air pocket DFS terminates
bounding_box = [
    [min(coords[0] for coords in lava) - 1, max(coords[0] for coords in lava) + 1],
    [min(coords[1] for coords in lava) - 1, max(coords[1] for coords in lava) + 1],
    [min(coords[2] for coords in lava) - 1, max(coords[2] for coords in lava) + 1],
]


def is_within_bounds(x, y, z):
    return (
        bounding_box[0][0] <= x <= bounding_box[0][1]
        and bounding_box[1][0] <= y <= bounding_box[1][1]
        and bounding_box[2][0] <= z <= bounding_box[2][1]
    )


directions = [(0, 0, 1), (0, 1, 0), (1, 0, 0), (0, 0, -1), (0, -1, 0), (-1, 0, 0)]


def get_total_surface_area(cubes):
    total_surface_area = 0
    surroundings = set()
    for x, y, z in cubes:
        for dx, dy, dz in directions:
            if (x + dx, y + dy, z + dz) not in cubes:
                total_surface_area += 1
                surroundings.add((x + dx, y + dy, z + dz))
    return total_surface_area, surroundings


total_surface_area, air = get_total_surface_area(lava)
print("Part 1:", total_surface_area)

# DFS to find air pockets
seen = set()
air_pockets = list()

for x, y, z in air:
    if (x, y, z) not in seen:
        air_pockets.append(set())
        dfs = [(x, y, z)]
        while dfs:
            ex, ey, ez = dfs.pop()
            seen.add((ex, ey, ez))
            air_pockets[-1].add((ex, ey, ez))
            for dx, dy, dz in directions:
                nx, ny, nz = ex + dx, ey + dy, ez + dz
                if (
                    is_within_bounds(nx, ny, nz)
                    and (nx, ny, nz) not in seen
                    and (nx, ny, nz) not in lava
                ):
                    dfs.append((nx, ny, nz))

internal_surface_area = 0
size_of_outside_air = max(len(pocket) for pocket in air_pockets)
for pocket in air_pockets:
    if len(pocket) < size_of_outside_air:
        internal_surface_area += get_total_surface_area(pocket)[0]

print("Part 2:", total_surface_area - internal_surface_area)
