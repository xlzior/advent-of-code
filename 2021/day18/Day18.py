from __future__ import annotations  # for self-referential type annotations
from typing import Tuple

import json
import math
import sys
import functools
from itertools import permutations

class Element:
    def __init__(self, element_type, value: int = None, left: Element = None, right: Element = None):
        self.element_type = element_type
        self.value = value
        self.set_left(left)
        self.set_right(right)
        self.up: Tuple[Element | None, Element | None] = (None, None)

    def set_left(self, left):  # update left child's parent pointer too
        self.left = left
        if self.left:
            self.left.up = (self, "left")

    def set_right(self, right):  # update right child's parent pointer too
        self.right = right
        if self.right:
            self.right.up = (self, "right")

    def __add__(self, snail2):
        result = Element("pair", left=self, right=snail2)
        result.reduce()
        return result

    def __str__(self):
        if self.element_type == "number":
            return f"{self.value}"
        else:
            return f"[{str(self.left)}, {str(self.right)}]"

    def find_min(self):
        return self.left.find_min() if self.left else self

    def find_max(self):
        return self.right.find_max() if self.right else self

    def increment_predecessor_by(self, value):  # go up until a right child, then go left and right all the way
        if self.up[0]:
            if self.up[1] == "right":
                self.up[0].left.find_max().value += value
            else:
                self.up[0].increment_predecessor_by(value)

    def increment_successor_by(self, value):  # go up until a left child, then go right and left all the way
        if self.up[0]:
            if self.up[1] == "left":
                self.up[0].right.find_min().value += value
            else:
                self.up[0].increment_successor_by(value)

    def find_nested_pair(self, n) -> Element | None:
        if self.element_type == "number":
            return None
        if n == 0:
            return self
        return self.left.find_nested_pair(n - 1) or self.right.find_nested_pair(n - 1)

    def explode(self):
        self.increment_predecessor_by(self.left.value)
        self.increment_successor_by(self.right.value)
        self.element_type = "number"
        self.value = 0
        self.set_left(None)
        self.set_right(None)
        return True

    def find_and_explode(self):  # returns whether a pair was exploded
        pair_to_explode = self.find_nested_pair(4)
        return pair_to_explode and pair_to_explode.explode()

    def find_large_number(self) -> Element | None:
        if self.element_type == "number":
            return self if self.value >= 10 else None
        return self.left.find_large_number() or self.right.find_large_number()

    def split(self):
        self.element_type = "pair"
        self.set_left(Element("number", value=math.floor(self.value / 2)))
        self.set_right(Element("number", value=math.ceil(self.value / 2)))
        self.value = None
        return True

    def find_and_split(self):  # returns whether a pair was split
        pair_to_split = self.find_large_number()
        return pair_to_split and pair_to_split.split()

    def reduce(self):
        while self.find_and_explode() or self.find_and_split():  # inspired by Reddit
            pass

    def magnitude(self):
        if self.element_type == "number":
            return self.value
        else:
            return 3 * self.left.magnitude() + 2 * self.right.magnitude()


def create_element(string):
    def helper(item):
        if type(item) is int:
            return Element("number", value=item)
        else:
            return Element("pair", left=helper(item[0]), right=helper(item[1]))
    return helper(json.loads(string))


with open(sys.argv[1]) as file:
    puzzle_input = file.read().split("\n")

elements = [create_element(line) for line in puzzle_input]
combined = functools.reduce(lambda x, y: x + y, elements)
print("Part 1:", combined.magnitude())

max_seen = 0
for i, j in permutations(list(range(len(puzzle_input))), 2):
    curr = create_element(puzzle_input[i]) + create_element(puzzle_input[j])
    max_seen = max(max_seen, curr.magnitude())
print("Part 2:", max_seen)
