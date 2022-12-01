import sys
from Computer import Computer
from Point import Point, deltas

BLACK = 0
WHITE = 1
TURN_LEFT = {"UP": "LEFT", "LEFT": "DOWN", "DOWN": "RIGHT", "RIGHT": "UP"}
TURN_RIGHT = {"UP": "RIGHT", "RIGHT": "DOWN", "DOWN": "LEFT", "LEFT": "UP"}
TURN = [TURN_LEFT, TURN_RIGHT]

puzzle_input = open(sys.argv[1]).read().strip()
program = list(map(int, puzzle_input.split(",")))

def paint_hull(starting_colour):
    computer = Computer(program.copy(), [])

    panels_painted = set()
    white_panels = set()
    current_location = Point(0, 0)
    if starting_colour == WHITE:
        white_panels.add(current_location)
    current_direction = "UP"

    while computer.state != Computer.HALTED:
        current_colour = WHITE if current_location in white_panels else BLACK
        colour, turn_direction = computer.resume([current_colour])

        panels_painted.add(current_location)
        if colour == WHITE:
            white_panels.add(current_location)
        elif current_colour == WHITE and colour == BLACK:
            white_panels.remove(current_location)

        current_direction = TURN[turn_direction][current_direction]
        current_location += deltas[current_direction]

    return panels_painted, white_panels


# Part 1
panels_painted, _ = paint_hull(BLACK)
print("Part 1:", len(panels_painted))


# Part 2
print("Part 2:")
_, white_panels = paint_hull(WHITE)
min_x = min(map(lambda panel: panel.x, white_panels))
max_x = max(map(lambda panel: panel.x, white_panels))
min_y = min(map(lambda panel: panel.y, white_panels))
max_y = max(map(lambda panel: panel.y, white_panels))

for y in range(min_y, max_y + 1):
    row = list()
    for x in range(min_x, max_x + 1):
        if Point(x, y) in white_panels:
            row.append(chr(9608))
        else:
            row.append(" ")
    print("".join(row))
