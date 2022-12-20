import sys, re, collections, numpy as np

V = lambda *a: np.array(a)


def parse(line):
    id, a, b, c, d, e, f = map(int, re.findall(r"\d+", line))
    return (
        id,
        (V(1, 0, 0, 0), V(0, f, 0, e)),
        (V(0, 1, 0, 0), V(0, 0, d, c)),
        (V(0, 0, 1, 0), V(0, 0, 0, b)),
        (V(0, 0, 0, 1), V(0, 0, 0, a)),
    )


def run(blueprint, time):
    def visit(time_left, robots, resources):
        # base case
        if time_left == 0:
            return resources[0]

        # pruning; not worth it to explore
        if any(robots > max_required):  # excessive robots
            count += 1
            return 0
        if (
            resources[0] + time_left * robots[0] + time_left * (time_left - 1) / 2
            <= best_so_far[time_left]
        ):  # even optimistically, can't catch up
            return 0

        # worth it to explore
        child_scores = list()
        for more_robot, cost in blueprint:
            # fast forward time to points of interest, when building robots
            temp = (cost - resources) / robots
            time_taken = max(0, np.nanmax(np.ceil(temp))) + 1
            if not any(np.isinf(temp)) and time_taken <= time_left - 1:
                result = visit(
                    time_left - time_taken,
                    robots + more_robot,
                    resources + time_taken * robots - cost,
                )
                child_scores.append(result)

        # don't build any more robots during remaining time
        child_scores.append(visit(0, robots, resources + time_left * robots))
        best_so_far[time_left] = max(best_so_far[time_left], max(child_scores))
        return max(child_scores)

    best_so_far = collections.defaultdict(int)
    max_required = np.max(V(*(b[1] for b in blueprint)), axis=0)
    max_required[0] = time
    return visit(time, V(0, 0, 0, 1), V(0, 0, 0, 0))


part_1_score = 0
part_2_score = 1

for id, *blueprint in map(parse, open(sys.argv[-1]).read().split("\n")):
    part_1_score += run(blueprint, 24) * id
    part_2_score *= run(blueprint, 32) if id <= 3 else 1

print("Part 1:", int(part_1_score))
print("Part 2:", int(part_2_score))
