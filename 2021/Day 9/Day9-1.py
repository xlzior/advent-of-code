import sys

with open(sys.argv[1]) as file:
    lines = file.readlines()
    lines = list(map(lambda x: x.strip(), lines))
    print(len(lines), len(lines[0]))


def get_height(x, y):
    if 0 <= y < len(lines) and 0 <= x < len(lines[0]):
        return int(lines[y][x])
    return 10000


total_risk = 0

for y in range(len(lines)):
    for x in range(len(lines[0])):
        is_minimum = True
        curr_height = get_height(x, y)
        for delta in [[0, 1], [0, -1], [1, 0], [-1, 0]]:
            [delta_x, delta_y] = delta
            if curr_height >= get_height(x + delta_x, y + delta_y):
                is_minimum = False
        if is_minimum:
            total_risk += curr_height + 1

print(total_risk)
