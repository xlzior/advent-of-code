import sys


def process_raw_line(raw_line):
    [src, dst] = raw_line.strip().split(" -> ")
    src = list(map(int, src.strip().split(",")))
    dst = list(map(int, dst.strip().split(",")))
    return [src, dst] if src[0] < dst[0] else [dst, src]


def is_horizontal(line):
    [[_, src_y], [_, dst_y]] = line
    return src_y == dst_y


def is_vertical(line):
    [[src_x, _], [dst_x, _]] = line
    return src_x == dst_x


with open(sys.argv[1]) as file:
    lines = file.readlines()
lines = list(map(process_raw_line, lines))
horizontals = list(filter(is_horizontal, lines))
verticals = list(filter(is_vertical, lines))

horizontals.sort()
for i in range(10):
    print(horizontals[i])