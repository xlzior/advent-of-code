import sys, re, collections

sub = lambda x: x[0] - x[1]

with open(sys.argv[-1]) as file:
    puzzle_input = file.read().split("\n")

for line in puzzle_input[:1]:
    nums = list(map(int, re.findall(r"(\d+)", line)))
    id = nums[0]
    robot_costs = [
        (nums[5], 0, nums[6], 0),  # geode robot cost
        (nums[3], nums[4], 0, 0),  # obsidian robot cost
        (nums[2], 0, 0, 0),  # clay robot cost
        (nums[1], 0, 0, 0),  # ore robot cost
    ]
    max_robots_needed = [max(nums[1], nums[2], nums[3], nums[5]), nums[4], nums[6], 24]
    max_geodes = 0

    dfs = list()
    dfs.append((24, (1, 0, 0, 0), (0, 0, 0, 0)))
    while dfs:
        time_left, robots, resources = dfs.pop()
        print(time_left, robots, resources)
        new_resources = tuple(map(sum, zip(resources, robots)))
        if max_geodes < resources[-1]:
            print(robots, resources)
        max_geodes = max(max_geodes, resources[-1])
        if time_left == 0:
            continue

        dfs.append((time_left - 1, robots, new_resources))
        for i, cost in enumerate(robot_costs):
            if all(r >= c for r, c in zip(resources, cost)):
                new_robots = tuple(x + int(i == 3 - j) for j, x in enumerate(robots))
                new_resources = tuple(map(sub, zip(resources, cost)))
                dfs.append((time_left - 1, new_robots, new_resources))

    print(max_geodes)
