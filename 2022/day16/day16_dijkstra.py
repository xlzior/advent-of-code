import sys
import re
import heapq
import collections

with open(sys.argv[-1]) as file:
    puzzle_input = file.read().split("\n")

neighbours = dict()
flow_rates = dict()

for line in puzzle_input:
    name, rate, tunnels = re.match(
        r"Valve ([A-Z]+) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)",
        line,
    ).groups()
    neighbours[name] = tunnels.split(", ")
    flow_rates[name] = int(rate)

# terminate early if all valves are open?
# may not help speed things up much as there are 15 valves to open in 26 moves
max_valves_open = len(flow_rates) - list(flow_rates.values()).count(0)

pressure_released = collections.defaultdict(lambda: 99999)
total_time = 30
pq = [(0, "AA", total_time, frozenset())]
pressure_released[("AA", total_time, frozenset())] = 0
min_pressure_released = 0


def decrease_priority(pressure, valve, time_left, open_valves):
    if pressure < pressure_released[(valve, time_left, open_valves)]:
        pressure_released[(valve, time_left, open_valves)] = pressure
        heapq.heappush(pq, (pressure, valve, time_left, open_valves))


while pq:
    pr, valve, time_left, open_valves = heapq.heappop(pq)
    if time_left == 0:
        continue
    if valve not in open_valves and flow_rates[valve] > 0:
        new_open_valves = open_valves.union({valve})
        new_pr = (
            pressure_released[(valve, time_left, open_valves)]
            - (time_left - 1) * flow_rates[valve]
        )
        min_pressure_released = min(min_pressure_released, new_pr)
        decrease_priority(new_pr, valve, time_left - 1, new_open_valves)

    if len(open_valves) < max_valves_open:
        for neighbour in neighbours[valve]:
            decrease_priority(pr, neighbour, time_left - 1, open_valves)

print("Part 1:", -min_pressure_released)
