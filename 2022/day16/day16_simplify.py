import sys, re, queue, collections

with open(sys.argv[-1]) as file:
    puzzle_input = file.read().split("\n")

full_graph = dict()  # adjacency list for full graph
flow_rates = dict()
name_to_index = dict()

for line in puzzle_input:
    name, rate, tunnels = re.match(
        r"Valve ([A-Z]+) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)",
        line,
    ).groups()
    full_graph[name] = tunnels.split(", ")
    if int(rate) > 0 or name == "AA":
        name_to_index[name] = len(flow_rates)
        flow_rates[name] = int(rate)

simplified_graph = collections.defaultdict(list)

for src in flow_rates:
    todo = set(flow_rates.keys())
    bfs = queue.Queue()
    bfs.put((src, 0))
    seen = set()
    while todo and bfs:
        curr, distance = bfs.get()
        seen.add(curr)
        if curr in todo:
            todo.remove(curr)
            simplified_graph[src].append((curr, distance + 1))
        for neighbour in full_graph[curr]:
            if neighbour not in seen:
                bfs.put((neighbour, distance + 1))


def is_valve_open(open_valves, valve_name):
    return open_valves & (2 ** name_to_index[valve_name]) > 0


def toggle_valve(open_valves, valve_name):
    return open_valves ^ (1 << name_to_index[valve_name])


def run_bfs(src):
    max_pressure_released = 0
    paths = collections.defaultdict(int)
    bfs = queue.Queue()
    bfs.put(src)
    while not bfs.empty():
        valve_name, time_left, open_valves, pressure_released = bfs.get()
        pressure_released += time_left * flow_rates[valve_name]
        paths[open_valves] = max(paths[open_valves], pressure_released)
        max_pressure_released = max(max_pressure_released, pressure_released)
        for neighbour, distance in simplified_graph[valve_name]:
            if not is_valve_open(open_valves, neighbour) and distance < time_left:
                bfs.put(
                    (
                        neighbour,
                        time_left - distance,
                        toggle_valve(open_valves, neighbour),
                        pressure_released,
                    )
                )
    return max_pressure_released, paths


print("Part 1:", run_bfs(("AA", 30, 0, 0))[0])

paths = run_bfs(("AA", 26, 0, 0))[1]
part_2_answer = 0
for set1 in paths:
    for set2 in paths:
        if not set1 & set2:
            part_2_answer = max(part_2_answer, paths[set1] + paths[set2])

print("Part 2:", part_2_answer)
