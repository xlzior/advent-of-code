import collections
import sys
import re


class Guard:
    def __init__(self):
        self.minutes_asleep = 0
        self.counter = collections.Counter()

    def add_sleep(self, start, end):
        self.minutes_asleep += end - start
        self.counter.update(range(start, end))


# read input
with open(sys.argv[1]) as file:
    puzzle_input = file.read().split("\n")
    # input.txt is the sorted version of the puzzle input, so no sorting is necessary here

guards = collections.defaultdict(Guard)
guard_id = None
start = None
for line in puzzle_input:
    minute = int(re.search(r"\[1518-\d+-\d+ \d+:(\d+)\]", line).groups()[0])
    if "begins shift" in line:
        guard_id_match = re.search(r"Guard #(\d+) begins shift", line)
        guard_id = int(guard_id_match.groups()[0])
    elif "falls asleep" in line:
        start = minute
    elif "wakes up" in line:
        guards[guard_id].add_sleep(start, minute)


def part_1():  # sleepiest guard -> sleepiest minute
    sleepiest_id = None
    max_minutes_asleep = 0

    for guard_id in guards:
        if guards[guard_id].minutes_asleep > max_minutes_asleep:
            sleepiest_id = guard_id
            max_minutes_asleep = guards[guard_id].minutes_asleep

    sleepiest_minute = guards[sleepiest_id].counter.most_common(1)[0][0]
    print(f"Part 1: {sleepiest_id} * {sleepiest_minute} = {sleepiest_id * sleepiest_minute}")


def part_2():  # sleepiest minute overall
    guard_id = None
    minute = None
    count = 0

    for curr_id in guards:
        curr_minute, curr_count = guards[curr_id].counter.most_common(1)[0]
        if curr_count > count:
            guard_id = curr_id
            minute = curr_minute
            count = curr_count

    print(f"Part 2: {guard_id} * {minute} = {guard_id * minute}")


part_1()
part_2()
