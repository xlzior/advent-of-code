import sys

puzzle_input = open(sys.argv[-1]).read().split("\n")
stuff = set()
largest_y = 0

for line in puzzle_input:
    points = [tuple(map(int, point.split(","))) for point in line.split(" -> ")]
    lines = [points[i : i + 2] for i in range(len(points) - 1)]
    for p1, p2 in lines:
        x1, y1 = p1
        x2, y2 = p2
        largest_y = max(largest_y, y1, y2)
        if x2 < x1:
            x1, x2 = x2, x1
        if y2 < y1:
            y1, y2 = y2, y1
        for x in range(x1, x2 + 1):
            for y in range(y1, y2 + 1):
                stuff.add((x, y))

count = 0
part_1_done = False
while (500, 0) not in stuff:
    x, y = 500, 0
    at_rest = False
    while not at_rest:
        if y > largest_y:
            if not part_1_done:
                print("Part 1:", count)
                part_1_done = True
            at_rest = True
            stuff.add((x, y))
            count += 1
        elif (x, y + 1) not in stuff:
            y = y + 1
        elif (x - 1, y + 1) not in stuff:
            x, y = x - 1, y + 1
        elif (x + 1, y + 1) not in stuff:
            x, y = x + 1, y + 1
        else:
            at_rest = True
            stuff.add((x, y))
            count += 1

print("Part 2:", count)
