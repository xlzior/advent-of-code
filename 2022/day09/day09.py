import sys


def get_unit_vector(n):
    if n == 0:
        return 0
    return 1 if n > 0 else -1


def get_new_pos(head, tail):
    hr, hc = head
    tr, tc = tail
    if abs(hr - tr) <= 1 and abs(hc - tc) <= 1:
        return tr, tc
    else:
        dr, dc = hr - tr, hc - tc
        return tr + get_unit_vector(dr), tc + get_unit_vector(dc)


DIRECTIONS = {"U": (-1, 0), "D": (1, 0), "L": (0, -1), "R": (0, 1)}
puzzle_input = open(sys.argv[-1]).read().split("\n")
instructions = list(map(lambda x: (x[0], int(x.split(" ")[-1])), puzzle_input))


def track_tail(n):
    rope = [(0, 0) for _ in range(n)]
    visited = set()

    for dir, count in instructions:
        dr, dc = DIRECTIONS[dir]
        for _ in range(count):
            rope[0] = rope[0][0] + dr, rope[0][1] + dc
            for i in range(1, n):
                rope[i] = get_new_pos(rope[i - 1], rope[i])
            visited.add(rope[n - 1])

    return len(visited)


print("Part 1:", track_tail(2))
print("Part 2:", track_tail(10))
