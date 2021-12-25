import sys

SOUTH = "v"
EAST = ">"

def next_spot(x, y, direction):
    if direction == EAST:
        return (x + 1) % width, y
    elif direction == SOUTH:
        return x, (y + 1) % height

def move_herd(cucumbers, direction):
    a_cucumber_moved = False
    next_cucumbers = set()
    for x, y in cucumbers:
        destination = next_spot(x, y, direction)
        if destination in east_cucumbers or destination in south_cucumbers:
            next_cucumbers.add((x, y))
        else:
            a_cucumber_moved = True
            next_cucumbers.add(destination)
    return next_cucumbers, a_cucumber_moved


puzzle_input = open(sys.argv[1]).read().split("\n")
height = len(puzzle_input)
width = len(puzzle_input[0])
east_cucumbers = set()
south_cucumbers = set()

for y in range(height):
    for x in range(width):
        direction = puzzle_input[y][x]
        if direction == EAST:
            east_cucumbers.add((x, y))
        elif direction == SOUTH:
            south_cucumbers.add((x, y))

count = 0
a_cucumber_moved = True
while a_cucumber_moved:
    count += 1
    east_cucumbers, an_east_cucumber_moved = move_herd(east_cucumbers, EAST)     # east-facing herd moves first,
    south_cucumbers, a_south_cucumber_moved = move_herd(south_cucumbers, SOUTH)  # then the south-facing herd moves
    a_cucumber_moved = an_east_cucumber_moved or a_south_cucumber_moved

print(count)
