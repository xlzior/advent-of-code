import sys
import re


class Trajectory:
    def __init__(self, x_velocity, y_velocity):
        self.x = 0
        self.y = 0
        self.starting_conditions = (x_velocity, y_velocity)
        self.x_velocity = x_velocity
        self.y_velocity = y_velocity
        self.highest_y = 0

    def calculate_drag(self):
        if self.x_velocity > 0:
            return -1
        elif self.x_velocity < 0:
            return 1
        return 0

    def step(self):
        self.x += self.x_velocity
        self.y += self.y_velocity
        self.x_velocity += self.calculate_drag()
        self.y_velocity -= 1
        self.highest_y = max(self.highest_y, self.y)
        # print(self.x, self.y)

    def reaches_target(self):
        while self.x <= end_x:  # repeat until overshoot in x direction
            self.step()
            if start_x <= self.x <= end_x and start_y <= self.y <= end_y:
                return True
            elif self.x < start_x and self.calculate_drag() == 0:  # impossible to reach (start_x, end_x)
                return False
        return False

    def __str__(self):
        return str(self.starting_conditions)


with open(sys.argv[1]) as file:
    puzzle_input = file.read().strip()

match = re.match(r"target area: x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)", puzzle_input)
start_x, end_x, start_y, end_y = map(int, match.groups())

# print(Trajectory(7, 2).reaches_target())
# print(Trajectory(6, 3).reaches_target())
# print(Trajectory(9, 0).reaches_target())
# print(Trajectory(17, -4).reaches_target())

# candidates = [Trajectory(x, y) for x in range(1, end_x) for y in range(10)]
# filtered_candidates = filter(lambda candidate: candidate.reaches_target(), candidates)
# best_candidate = max(filtered_candidates, key=lambda candidate: candidate.highest_y)

# print(best_candidate)
# print(best_candidate.highest_y)

print(Trajectory(7, -1).reaches_target())
