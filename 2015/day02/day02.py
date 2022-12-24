import re

puzzle_input = open("puzzle.txt").read().split("\n")
wrapping_paper = 0
ribbon = 0
for line in puzzle_input:
    l, w, h = map(int, re.findall("\d+", line))
    wrapping_paper += 2 * (l * w + w * h + h * l) + min(l * w, w * h, h * l)
    ribbon += 2 * (l + w + h - max(l, w, h)) + l * w * h

print("Part 1:", wrapping_paper)
print("Part 2:", ribbon)
