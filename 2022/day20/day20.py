import sys


class Node:
    def __init__(self, ll, value, prev, next) -> None:
        self.ll = ll
        self.value = value
        self.prev = prev
        self.next = next

    def shift(self, n):
        if n > 0:
            self.shift_right(n)
        elif n < 0:
            self.shift_left(-n)

    def shift_right(self, n):
        # remove from this part
        self.prev.next = self.next
        self.next.prev = self.prev

        # iterate to find insertion point
        self.prev = self
        for _ in range(n):
            self.prev = self.prev.next

        # insert into insertion point
        self.next = self.prev.next
        self.prev.next = self
        self.next.prev = self

    def shift_left(self, n):
        # remove from this part
        self.prev.next = self.next
        self.next.prev = self.prev

        # iterate to find insertion point
        self.next = self
        for _ in range(n):
            self.next = self.next.prev

        # insert into insertion point
        self.prev = self.next.prev
        self.prev.next = self
        self.next.prev = self


class LinkedList:
    def __init__(self, l) -> None:
        self.len = len(l)
        self.start = curr = original_start = Node(self, l[0], None, None)
        self.original_order = [curr]
        for i in l[1:]:
            curr.next = Node(self, i, curr, None)
            curr = curr.next
            if i == 0:  # use 0 as the start for convenience later
                self.start = curr
            self.original_order.append(curr)

        # turn into a cycle
        curr.next = original_start
        original_start.prev = curr

    def get(self, i):
        curr = self.start
        for _ in range(i):
            curr = curr.next
        return curr


def mix(ll, n):
    for _ in range(n):
        for node in ll.original_order:
            node.shift(node.value % (ll.len - 1))


def get_grove_score(ll):
    return sum(ll.get(i).value for i in [1000, 2000, 3000])


nums = list(map(int, open(sys.argv[1]).read().split("\n")))

ll1 = LinkedList(nums)
mix(ll1, 1)
print("Part 1:", get_grove_score(ll1))

ll2 = LinkedList([num * 811589153 for num in nums])
mix(ll2, 10)
print("Part 2:", get_grove_score(ll2))
