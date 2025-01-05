import re
import sys

import numpy as np

lines = open(sys.argv[1]).read().split("\n")
ingredients = [[int(i) for i in re.findall(r"(-?\d+)", line)] for line in lines]

np_ingredients = np.array(ingredients)

part1 = 0
part2 = 0
for a in range(100):
    for b in range(100 - a):
        for c in range(100 - a - b):
            d = 100 - a - b - c
            assert d >= 0
            np_recipe = np.array([a, b, c, d])
            cookie_stats = np_recipe @ np_ingredients
            scores = np.clip(np.delete(cookie_stats, -1, 0), 0, None)
            score = np.prod(scores)
            part1 = max(part1, score)
            if cookie_stats[-1] == 500:
                part2 = max(part2, score)


print("Part 1:", part1)
print("Part 2:", part2)
