import sys

def get_surrounding_9(x, y):
    return "".join(["".join(image[j][x - 1:x + 2]) for j in range(y - 1, y + 2)])

def enhance(i):
    if algorithm[0] == "0":  # dark pixels stay dark -> background is empty
        background = "0"
    elif algorithm[0] == "1" and algorithm[-1] == "0":  # background pixels alternate between light and dark
        background = "1" if i % 2 == 0 else "0"
    new_image = [[background for _ in range(buffered_width)] for _ in range(buffered_height)]
    for y in range(1, buffered_height - 1):
        for x in range(1, buffered_width - 1):
            surrounding_pixels = int(get_surrounding_9(x, y), 2)
            new_image[y][x] = algorithm[surrounding_pixels]
    return new_image


with open(sys.argv[1]) as file:
    algorithm, initial = file.read().split("\n\n")
    assert len(algorithm) == 512
    algorithm = algorithm.replace(".", "0").replace("#", "1")
    initial = initial.split("\n")

rounds = int(sys.argv[2])
height = len(initial)
width = len(initial[0])
buffer = rounds + 2
buffered_height = height + 2 * buffer
buffered_width = width + 2 * buffer

image = [["0" for i in range(buffered_width)] for j in range(buffered_height)]

# copy over the initial image to the buffered array
for y in range(height):
    for x in range(width):
        image[y + buffer][x + buffer] = "0" if initial[y][x] == "." else "1"

for i in range(rounds):
    image = enhance(i)

print(sum(list(map(lambda row: sum(map(int, row)), image))))
