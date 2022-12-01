import collections
import sys

# attempt 2: iterate through a linked list and insert in O(1) (still too slow for part 2)


class Node:
    def __init__(self, char):
        self.char = char
        self.next = None

    def insert_after(self, node):
        node.next = self.next
        self.next = node

    def stringify(self):
        if self.next:
            return self.char + self.next.stringify()
        return self.char


with open(sys.argv[1]) as file:
    chain, rules = file.read().split("\n\n")
    rules = dict(map(lambda line: line.split(" -> "), rules.split("\n")))

counter = collections.Counter(chain)

# initialise my linked list with chain
start = Node(chain[0])
end = start
for char in chain[1:]:
    new_node = Node(char)
    end.insert_after(new_node)
    end = new_node

for _ in range(15):
    curr = start
    while curr.next:
        pair = curr.char + curr.next.char
        if pair in rules:
            counter.update(rules[pair])
            curr.insert_after(Node(rules[pair]))
            curr = curr.next  # newly-inserted not considered until the next step
        curr = curr.next


counter_rankings = counter.most_common()
most_common = counter_rankings[0]
least_common = counter_rankings[-1]
print(most_common, least_common)
print(most_common[1] - least_common[1])
