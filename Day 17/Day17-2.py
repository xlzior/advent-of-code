import sys
import re
from abc import ABC, abstractmethod


class Trajectory(ABC):
    def __init__(self, velocity):
        self.coordinate = self.highest = 0
        self.initial = self.velocity = velocity
        self.reaches_target = False
        self.steps_taken = 0
        self.steps_taken_range = [-1, -1]
        self.simulate()

    @abstractmethod
    def get_acceleration(self):
        pass

    def step(self):
        self.steps_taken += 1
        self.coordinate += self.velocity
        self.velocity += self.get_acceleration()
        self.highest = max(self.highest, self.coordinate)

    @abstractmethod
    def simulate(self):
        pass

    def update_range(self):
        if not self.reaches_target:  # first time reaching target
            self.reaches_target = True
            self.steps_taken_range = [self.steps_taken, self.steps_taken]
        else:
            self.steps_taken_range[1] = self.steps_taken


class XTrajectory(Trajectory):
    def get_acceleration(self):
        if self.velocity > 0:
            return -1
        elif self.velocity < 0:
            return 1
        return 0

    def simulate(self):
        while self.coordinate <= right_x:  # repeat until overshoot in x direction (positive)
            self.step()
            if self.reaches_target and self.get_acceleration() == 0:  # will always be in the x target range
                self.steps_taken_range[1] = 10000000                  # simulate infinity
                break
            if left_x <= self.coordinate <= right_x:
                self.update_range()
            elif self.coordinate < left_x and self.get_acceleration() == 0:  # impossible to reach (left_x, right_x)
                break


class YTrajectory(Trajectory):
    def get_acceleration(self):
        return -1

    def simulate(self):
        while self.coordinate >= bottom_y:  # repeat until overshot in y direction (negative)
            self.step()
            if bottom_y <= self.coordinate <= top_y:
                self.update_range()


def ranges_intersect(range_1, range_2):
    start_1, end_1 = range_1
    start_2, end_2 = range_2
    if end_1 < start_2 and start_1 < end_2:
        return False
    elif end_2 < start_1 and start_2 < end_1:
        return False
    return True


puzzle_input = open(sys.argv[1]).read().strip()
match = re.match(r"target area: x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)", puzzle_input)
left_x, right_x, bottom_y, top_y = map(int, match.groups())

min_x, max_x = 0, right_x + 10  # searching range
min_y, max_y = -400, 400

xs = list(filter(lambda x: x.reaches_target, [XTrajectory(x) for x in range(min_x, max_x)]))
ys = list(filter(lambda y: y.reaches_target, [YTrajectory(y) for y in range(min_y, max_y)]))

best_y = max(ys, key=lambda candidate: candidate.highest)
print("Part 1:", best_y.highest)  # fluke, assumes that there exists an x for this y

initial_velocities = [(x.initial, y.initial) for x in xs for y in ys
                      if ranges_intersect(x.steps_taken_range, y.steps_taken_range)]
print("Part 2:", len(initial_velocities))
