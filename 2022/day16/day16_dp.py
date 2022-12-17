import sys
import re
import collections


def flatten(list):
    return [ele for sublist in list for ele in sublist]


def is_valve_open(open_valves, valve_name):
    return open_valves & (2 ** name_to_index[valve_name]) > 0


def close_this_valve(open_valves, valve_name):
    return open_valves ^ (1 << name_to_index[valve_name])


with open(sys.argv[-1]) as file:
    puzzle_input = file.read().split("\n")

adj_list = dict()
flow_rates = dict()
max_open_valves = 0
name_to_index = dict()

for line in puzzle_input:
    name, rate, tunnels = re.match(
        r"Valve ([A-Z]+) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)",
        line,
    ).groups()
    adj_list[name] = tunnels.split(", ")
    flow_rates[name] = int(rate)
    if int(rate) > 0:
        name_to_index[name] = max_open_valves
        max_open_valves += 1

with_n_open_valves = collections.defaultdict(list)
for i in range(2**max_open_valves):
    with_n_open_valves[i.bit_count()].append(i)
open_valve_permutations = flatten(with_n_open_valves.values())

part_1_max = 0
dp = collections.defaultdict(int)

for my_time_left in range(2, 0, -1):
    for open_valves in open_valve_permutations:
        for my_pos in adj_list:
            came_from_neighbour = max(
                dp[(my_time_left + 1, open_valves, my_neighbour)]
                for my_neighbour in adj_list[my_pos]
            )

            just_opened_this_valve = 0
            if flow_rates[my_pos] > 0 and is_valve_open(open_valves, my_pos):
                just_opened_this_valve = (
                    dp[
                        (
                            my_time_left + 1,
                            close_this_valve(open_valves, my_pos),
                            my_pos,
                        )
                    ]
                    + (my_time_left - 1) * flow_rates[my_pos]
                )

            dp[(my_time_left, open_valves, my_pos)] = max(
                came_from_neighbour, just_opened_this_valve
            )

print(max(dp.values()))
