import sys

with open(sys.argv[1]) as file:
    lines = file.read()
    str_dots, instructions = list(map(lambda x: x.split("\n"), lines.split("\n\n")))


def fold_over(dot, axis, line):
    x1, y1 = list(map(int, dot.split(",")))
    x2, y2 = {
        "x": [x1, y1] if x1 <= line else [line * 2 - x1, y1],
        "y": [x1, y1] if y1 <= line else [x1, line * 2 - y1]
    }[axis]
    return f"{x2},{y2}"


print("Before folding:", len(str_dots))
for fold in instructions:
    axis, line = fold.strip("fold along ").split("=")
    str_dots = set(fold_over(dot, axis, int(line)) for dot in str_dots)
    print(len(str_dots))  # part 1

dots = [list(map(int, dot.split(","))) for dot in str_dots]  # convert "x,y" into [x, y]

width = max(x for x, y in dots) + 1
height = max(y for x, y in dots) + 1
paper = [[" "] * width for i in range(height)]

for x, y in dots:
    paper[y][x] = "#"

print("\n".join(map(lambda row: " ".join(row), paper)))  # part 2
