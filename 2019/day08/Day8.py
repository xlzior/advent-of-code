import collections
import sys

raw_image, height, width = open(sys.argv[1]).read().strip().split("\n")
height = int(height)
width = int(width)
area = height * width

layers = [raw_image[i:i + area] for i in range(0, len(raw_image), area)]
layer_counters = map(collections.Counter, layers)
layer_of_interest = min(layer_counters, key=lambda layer: layer['0'])
print("Part 1:", layer_of_interest['1'] * layer_of_interest['2'])

image = [[" " for i in range(width)] for j in range(height)]
for y in range(height):
    for x in range(width):
        pixels = map(lambda layer: layer[y * width + x], layers)
        non_transparent = list(filter(lambda pixel: pixel != "2", pixels))
        image[y][x] = " " if non_transparent[0] == "0" else chr(9608)

print("Part 2:")
print("\n".join(map(lambda row: "".join(row), image)))
