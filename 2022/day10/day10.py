import sys

history = [1, 1]
for instruction in open(sys.argv[-1]).read().split("\n"):
    history.append(history[-1])
    if instruction.startswith("addx"):
        history.append(history[-1] + int(instruction.split(" ")[-1]))

print("Part 1:", sum(i * history[i] for i in [220, 180, 140, 100, 60, 20]))

for r in range(6):
    for c in range(40):
        print("#" if abs(c - history[r * 40 + c + 1]) <= 1 else " ", end="")
    print()
