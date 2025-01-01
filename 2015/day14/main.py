from collections import defaultdict
import re
import sys


lines = open(sys.argv[1]).read().split("\n")
reindeer = [list(map(int, re.findall(r"(\d+)", line))) for line in lines]


def get_distance(speed, fly_time, rest_time, time):
    full_units, leftover_time = divmod(time, fly_time + rest_time)
    distance = speed * (fly_time * full_units + min(fly_time, leftover_time))
    return distance


def get_points(reindeer):
    t = 1
    points = defaultdict(int)

    while t <= time:
        snapshot = [
            get_distance(speed, fly_time, rest_time, t)
            for speed, fly_time, rest_time in reindeer
        ]
        lead = max(snapshot)
        for i, distance in enumerate(snapshot):
            if distance == lead:
                points[i] += 1
        t += 1

    return points


time = 1000 if sys.argv[1] == "1.in" else 2503
part1 = max(
    get_distance(speed, fly_time, rest_time, time)
    for speed, fly_time, rest_time in reindeer
)
print("Part 1:", part1)

part2 = max(get_points(reindeer).values())
print("Part 2:", part2)
