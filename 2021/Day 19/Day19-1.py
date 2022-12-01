from __future__ import annotations  # for self-referential type annotations

import collections
import sys
from itertools import combinations
import numpy as np

import time

class Point:
    def __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y, self.z + other.z)

    def __sub__(self, other):
        return Point(self.x - other.x, self.y - other.y, self.z - other.z)

    def __mul__(self, other):
        return self.x * other.x + self.y * other.y + self.z * other.z

    def __str__(self):
        return f"{self.x},{self.y},{self.z}"

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y and self.z == other.z

    def __hash__(self):
        return hash(self.__str__())

    def get_coordinates(self):
        return self.x, self.y, self.z


def get_overlaps(delta_list1, delta_list2):
    overlaps = list()
    for delta1 in delta_list1:
        for delta2 in delta_list2:
            if delta1.overlaps_with(delta2):
                overlaps.append((delta1, delta2))
    return overlaps

class Delta:
    def __init__(self, point1: Point, point2: Point):
        zipped_coords = list(zip(point1.get_coordinates(), point2.get_coordinates()))
        self.point1 = point1
        self.point2 = point2
        self.absolute_diff = sorted(list(map(lambda tup: abs(tup[0] - tup[1]), zipped_coords)))
        self.differences = list(map(lambda tup: tup[0] - tup[1], zipped_coords))

    def __str__(self):
        return f"{self.point1} - {self.point2} = {self.differences}"
        # return f"{self.differences}"

    def __neg__(self):
        return Delta(self.point2, self.point1)

    def overlaps_with(self, other: Delta):
        return self.absolute_diff == other.absolute_diff

def beacons_to_points(raw_beacons):
    points = list()
    for beacon in raw_beacons.split("\n")[1:]:
        x, y, z = map(int, beacon.split(","))
        points.append(Point(x, y, z))
    return points

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

class Scanner:
    def __init__(self, raw_beacons):
        self.raw_beacons = beacons_to_points(raw_beacons)  # relative to self, not absolute
        self.deltas = [Delta(point1, point2) for point1, point2 in combinations(self.raw_beacons, 2)]
        self.overlaps_with = dict()

    def transform(self, point):
        result = []
        for i in range(3):
            col = Point(self.matrix[0][i], self.matrix[1][i], self.matrix[2][i])
            result.append(point * col)
        return Point(result[0], result[1], result[2])

    def transform_and_translate(self, point):
        return self.transform(point) + self.position

    def resolve_first(self):
        self.position = Point(0, 0, 0)
        self.matrix = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]  # identity matrix
        self.beacons = self.raw_beacons.copy()

    def resolve(self, basis):
        overlaps = self.overlaps_with[basis]
        reference = scanners[basis]

        my_delta, reference_delta = overlaps[0]  # look at the first overlap to derive matrix and position
        reference_point = reference_delta.point1
        possible_corresponding_points = {my_delta.point1, my_delta.point2}
        for overlap in overlaps[1:]:  # find a second overlap so we can identify the corresponding point
            my_next_delta, other_next_delta = overlap
            if other_next_delta.point1 == reference_point or other_next_delta.point2 == reference_point:
                if my_next_delta.point1 in possible_corresponding_points:
                    my_point = my_next_delta.point1
                elif my_next_delta.point2 in possible_corresponding_points:
                    my_point = my_next_delta.point2
                break

        if my_delta.point2 == my_point:
            my_delta = -my_delta

        ref_ref_point = reference.transform_and_translate(reference_point)
        self.matrix = np.matmul(get_transformation_matrix(my_delta, reference_delta), reference.matrix)
        self.position = ref_ref_point - self.transform(my_point)
        self.beacons = list(map(self.transform_and_translate, self.raw_beacons))


start_time = time.time()
with open(sys.argv[1]) as file:
    puzzle_input = file.read().split("\n\n")

scanners = [Scanner(chunk) for chunk in puzzle_input]
neighbours = collections.defaultdict(list)

OVERLAP_REQUIRED = 12

for i, j in combinations(range(len(puzzle_input)), 2):
    overlaps = get_overlaps(scanners[i].deltas, scanners[j].deltas)
    if len(overlaps) >= OVERLAP_REQUIRED * (OVERLAP_REQUIRED - 1) / 2:
        neighbours[i].append(j)
        neighbours[j].append(i)
        scanners[i].overlaps_with[j] = overlaps  # overlaps is a list of tuples with (my_delta, other_delta)
        scanners[j].overlaps_with[i] = list(map(lambda tup: (tup[1], tup[0]), overlaps))

scanners[0].resolve_first()
standardised_beacons = set(scanners[0].beacons)
resolved = {0}  # 0 is resolved
resolvable = list(map(lambda x: (0, x), neighbours[0]))  # neighbours of scanner 0 are ready to be resolved
while len(resolvable) > 0:
    basis, to_resolve = resolvable.pop()
    if to_resolve not in resolved:
        scanners[to_resolve].resolve(basis)
        resolved.add(to_resolve)
        standardised_beacons.update(scanners[to_resolve].beacons)
        resolvable.extend(map(lambda x: (to_resolve, x), neighbours[to_resolve]))

for scanner in scanners:
    print(scanner.position)

print(len(standardised_beacons))
print("--- %s seconds ---" % (time.time() - start_time))
