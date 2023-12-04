import collections
import sys

# attempt 1: brute force using regular strings (works for part 1, too slow for part 2)

with open(sys.argv[1]) as file:
    chain, rules = file.read().split("\n\n")
    rules = dict(map(lambda line: line.split(" -> "), rules.split("\n")))


for _ in range(10):
    insertion_points = []
    for i in range(len(chain) - 1):
        pair = chain[i:i+2]
        if pair in rules:
            insertion_points.append((i + 1, rules[pair]))

    for index, char in reversed(insertion_points):
        chain = chain[:index] + char + chain[index:]

counter = collections.Counter(chain)
counter_rankings = counter.most_common()
most_common = counter_rankings[0]
least_common = counter_rankings[-1]
print(most_common, least_common)
print(most_common[1] - least_common[1])
