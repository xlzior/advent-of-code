import sys
from collections import Counter


def find_marker(packet, n):
    counter = Counter(packet[: n - 1])
    left = 0
    for c in packet[n - 1 :]:
        counter[c] += 1
        if len(counter) == n:
            return left + n
        counter[packet[left]] -= 1
        if counter[packet[left]] == 0:
            del counter[packet[left]]
        left += 1


test_cases = [
    ("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7, 19),
    ("bvwbjplbgvbhsrlpgdmjqwftvncz", 5, 23),
    ("nppdvjthqldpwncqszvftbrmjlhg", 6, 23),
    ("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10, 29),
    ("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11, 26),
]

for test_case, part_1, part_2 in test_cases:
    assert find_marker(test_case, 4) == part_1
    assert find_marker(test_case, 14) == part_2

puzzle_input = open(sys.argv[1]).read()
print("Part 1:", find_marker(puzzle_input, 4))
print("Part 2:", find_marker(puzzle_input, 14))
