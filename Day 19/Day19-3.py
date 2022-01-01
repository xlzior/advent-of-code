import sys
from itertools import combinations
import numpy as np
import time

# Refactored Day19-1.py using an optimisation I saw on Reddit
# Day19-1
# - start by finding all chains of overlapping scanners to form a tree structure to be used for resolving
# - then resolve it one by one
# Day19-3
# - flattens this tree structure which reduces unnecessary work and simplifies it (somewhat?)
# - when scanners are resolved, add their beacons to scanner 0
# - scanner 0 keeps track of all known beacons (and known deltas)
# 19-1 runs in ~9s, 19-3 runs in ~1.5s, hence we can trivially combine part 2 into the same file

def beacons_to_points(raw_beacons):
    points = list()
    for beacon in raw_beacons.split("\n")[1:]:
        x, y, z = map(int, beacon.split(","))
        points.append(Vector(x, y, z))
    return points

class Vector:
    def __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z
        self.tuple = (x, y, z)

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y, self.z + other.z)

    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y, self.z - other.z)

    def __str__(self):
        return str(self.tuple)

    def manhattan(self, other):
        return abs(self.x - other.x) + abs(self.y - other.y) + abs(self.z - other.z)

class Delta:
    def __init__(self, point1: Vector, point2: Vector):
        self.points = (point1, point2)
        self.set = {point1, point2}
        self.differences = (point1 - point2).tuple
        self.absolute_differences = tuple(sorted(map(abs, self.differences)))

    def __eq__(self, other):
        return self.absolute_differences == other.absolute_differences

    def __hash__(self):
        return hash(self.absolute_differences)

    def __contains__(self, item):
        return item in self.set

    def __neg__(self):
        return Delta(self.points[1], self.points[0])

    def __str__(self):
        return f"{self.points[0]} - {self.points[1]} = {self.absolute_differences}"

    def contains_duplicates(self):  # if it contains duplicates e.g. [13 13 43], cannot find transformation accurately
        return self.absolute_differences[0] == self.absolute_differences[1] or\
               self.absolute_differences[1] == self.absolute_differences[2]


class Match:
    def __init__(self, delta1, delta2):
        self.deltas = (delta1, delta2)

    def __contains__(self, item):
        return item in self.deltas[0] or item in self.deltas[1]

    def __eq__(self, other):
        return self.__contains__(other)

    def __str__(self):
        return f"{self.deltas[0]} {self.deltas[1]}"

class Scanner:
    def __init__(self, raw_beacons):
        self.beacons = beacons_to_points(raw_beacons)
        self.deltas = dict((Delta(b1, b2), Delta(b1, b2)) for b1, b2 in combinations(self.beacons, 2))
        self.matched_deltas = set()
        self.matches = list()

    def find_matching_delta(self, known_delta):
        if known_delta in self.deltas and known_delta not in self.matched_deltas:
            self.matched_deltas.add(known_delta)
            my_delta = self.deltas[known_delta]
            self.matches.append(Match(known_delta, my_delta))

    def add_beacon(self, new_beacon):
        for beacon in self.beacons:
            new_delta = Delta(beacon, new_beacon)
            self.deltas[new_delta] = new_delta
        self.beacons.append(new_beacon)

    def manhattan(self, other):
        return self.coordinates.manhattan(other.coordinates)

    def resolve(self):
        # find rotation, as represented by a rotation matrix
        match1 = self.matches[0]
        reference_delta, my_delta = match1.deltas
        reference_point = reference_delta.points[0]

        # check for bad matches and restart if necessary
        filtered_matches = list(filter(lambda match: reference_point in match, self.matches[1:]))
        bad_reference = len(filtered_matches) == 0 or my_delta.contains_duplicates()
        if bad_reference and len(self.matches) > 66:
            self.matches = self.matches[1:]
            return self.resolve()

        # find corresponding point
        match2 = filtered_matches[0]
        my_point = my_delta.set.intersection(match2.deltas[1].set).pop()

        # check if my_delta is flipped compared to reference_delta
        if my_point == my_delta.points[1]:
            my_delta = -my_delta
        transformation_matrix = get_transformation_matrix(my_delta, reference_delta)

        # find translation, as represented by my scanner coordinates, with reference to the reference scanner
        my_coordinates = np.array(reference_point.tuple) - np.matmul(my_point.tuple, transformation_matrix)
        self.coordinates = Vector(my_coordinates[0], my_coordinates[1], my_coordinates[2])

        # add new beacons to reference scanner
        new_beacons = list(filter(lambda beacon: beacon not in self.matches, self.beacons))
        for new_beacon in new_beacons:
            x, y, z = np.matmul(new_beacon.tuple, transformation_matrix) + np.array(self.coordinates.tuple)
            reference_scanner.add_beacon(Vector(x, y, z))


def get_transformation_matrix(delta1: Delta, delta2: Delta):
    result = [[0 for _ in range(3)] for _ in range(3)]

    for i in range(3):
        if delta1.differences[i] in delta2.differences:
            index = delta2.differences.index(delta1.differences[i])
            result[i][index] = 1
        else:
            index = delta2.differences.index(-delta1.differences[i])
            result[i][index] = -1

    return result


start_time = time.time()
puzzle_input = open(sys.argv[1]).read().split("\n\n")

scanners = [Scanner(chunk) for chunk in puzzle_input]
reference_scanner = scanners[0]
reference_scanner.coordinates = Vector(0, 0, 0)
resolved = 1

while resolved < len(scanners):
    for i in range(resolved, len(scanners)):
        scanner = scanners[i]
        for delta in reference_scanner.deltas:
            scanner.find_matching_delta(delta)
        if len(scanner.matches) >= 66:
            scanner.resolve()
            scanners[resolved], scanners[i] = scanners[i], scanners[resolved]
            resolved += 1

print("Part 1:", len(reference_scanner.beacons))

max_seen = max(scanner1.manhattan(scanner2) for scanner1, scanner2 in combinations(scanners, 2))
print("Part 2:", max_seen)
print("--- %s seconds ---" % (time.time() - start_time))
