import sys, collections


def surroundings_are_clear(r, c):
    for dr in range(-1, 2):
        for dc in range(-1, 2):
            if not (dr == 0 and dc == 0) and (r + dr, c + dc) in elves:
                return False
    return True


def direction_is_clear(r, c, d):
    dr, dc = directions[d]
    for x in range(-1, 2):
        if dr != 0:  # row changes, go north or south
            if (r + dr, c + x) in elves:
                return False
        elif dc != 0:  # col changes, go west or east
            if (r + x, c + dc) in elves:
                return False
    return True


def get_next_elves(elves):
    next_elves = set()
    dst_to_src = collections.defaultdict(list)
    colliding_positions = set()
    some_elf_moved = False

    for r, c in elves:
        if surroundings_are_clear(r, c):
            next_elves.add((r, c))
        else:
            found_new_spot = False
            for j in range(4):
                d = (i + j) % 4
                if direction_is_clear(r, c, d):
                    dr, dc = directions[d]
                    new_pos = r + dr, c + dc
                    next_elves.add(new_pos)
                    some_elf_moved = True
                    dst_to_src[new_pos].append((r, c))
                    if len(dst_to_src[new_pos]) == 2:
                        colliding_positions.add(new_pos)
                    found_new_spot = True
                    break
            if not found_new_spot:
                next_elves.add((r, c))

    for colliding_pos in colliding_positions:
        next_elves.remove(colliding_pos)
        for original_pos in dst_to_src[colliding_pos]:
            next_elves.add(original_pos)

    return next_elves, some_elf_moved


def get_bounds(elves):
    min_r = min(r for r, c in elves)
    max_r = max(r for r, c in elves)
    min_c = min(c for r, c in elves)
    max_c = max(c for r, c in elves)
    return (min_r, max_r, min_c, max_c)


def get_dimensions(elves):
    min_r, max_r, min_c, max_c = get_bounds(elves)
    height = max_r - min_r + 1
    width = max_c - min_c + 1
    return height, width


def draw_map(elves):
    min_r, max_r, min_c, max_c = get_bounds(elves)
    for r in range(min_r, max_r + 1):
        for c in range(min_c, max_c + 1):
            print("#" if (r, c) in elves else ".", end="")
        print()


grid = open(sys.argv[-1]).read().split("\n")
h, w = len(grid), len(grid[0])
elves = {(r, c) for r in range(h) for c in range(w) if grid[r][c] == "#"}
directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

for i in range(1_000):
    elves, some_elf_moved = get_next_elves(elves)
    if i == 9:
        height, width = get_dimensions(elves)
        print("Part 1:", height * width - len(elves))
    if not some_elf_moved:
        print("Part 2:", i + 1)
        break
