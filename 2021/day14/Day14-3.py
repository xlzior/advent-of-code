import collections
import sys

# attempt 3: keep track of pairs present in a hashmap and update the counts accordingly (fast enough for part 2)
# key realisation:
# - originally I thought that position would matter hence we needed to store the full chain
#   e.g. in a string (attempt 1) or linked list (attempt 2)
# - but actually the overall position doesn't matter, only the counts of specific pairs of letters that exist
#   -> this is better represented as a dictionary / Counter object
#   - I lose information about where the pairs are, but that is unnecessary anyway

# initialise rules, letters, pairs
with open(sys.argv[1]) as file:
    chain, rules = file.read().split("\n\n")
    rules = dict(map(lambda line: line.split(" -> "), rules.split("\n")))  # dictionary representing insertion rules

letters = collections.Counter(chain)  # Counter object for letters, update it as I insert letters
pairs = collections.Counter()         # Counter object for pairs present in the chain
for i in range(len(chain) - 1):
    pair = chain[i:i + 2]
    pairs.update([pair])

# run the insertion 40 times
for _ in range(40):
    new_pairs = collections.defaultdict(int)
    for pair in pairs:
        if pair in rules:
            count = pairs[pair]                         # there are `count` instances of this pair,
            new_letter = rules[pair]                    # and I will insert `new_letter` into all of them
            letters.update({new_letter: count})         # update `letters` accordingly for easier calculation later
            left_neighbour = pair[0] + new_letter
            right_neighbour = new_letter + pair[1]
            new_pairs[left_neighbour] += count          # update `new_pairs` accordingly
            new_pairs[right_neighbour] += count
            new_pairs[pair] -= count

    pairs.update(new_pairs)                             # delay updating pairs until after the cycle
    # inserted elements are not considered to be part of a pair until the next step

# calculate the required numbers
counter_rankings = letters.most_common()
most_common = counter_rankings[0]
least_common = counter_rankings[-1]
print(most_common, least_common)
print(most_common[1] - least_common[1])
