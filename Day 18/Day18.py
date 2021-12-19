from __future__ import annotations

import json
import math
import sys
from typing import Tuple


class Element:
    def __init__(self, element_type, value: int = None, left: Element = None, right: Element = None):
        self.element_type = element_type
        self.value = value
        self.left = left
        self.right = right
        self.up: Tuple[Element | None, Element | None] = (None, None)
        if self.left:
            self.left.up = (self, "left")
        if self.right:
            self.right.up = (self, "right")

    def __add__(self, snail2):
        result = Element("pair", left=self, right=snail2)
        result.reduce()
        return result

    def __str__(self):
        if self.element_type == "pair":
            return f"[{str(self.left)}, {str(self.right)}]"
        else:
            return f"{self.value}"

    def find_min(self):
        return self.left.find_min() if self.left else self

    def find_max(self):
        return self.right.find_max() if self.right else self

    def find_predecessor(self):  # go up until a right child, then go left and right all the way
        if self.up[0]:
            if self.up[1] == "right":
                return self.up[0].left.find_max()
            else:
                return self.up[0].find_predecessor()

    def find_successor(self):  # go up until a left child, then go right and left all the way
        if self.up[0]:
            if self.up[1] == "left":
                return self.up[0].right.find_min()
            else:
                return self.up[0].find_successor()

    def find_nested(self, n) -> (Element | None, Element | None, Element | None):
        if self.element_type == "pair":
            if n == 0:
                return self, self.find_predecessor(), self.find_successor()

            left_nested = self.left.find_nested(n - 1)
            if left_nested[0]:
                return left_nested

            right_nested = self.right.find_nested(n - 1)
            if right_nested[0]:
                return right_nested

        return None, None, None

    def explode(self, left_neighbour: Element, right_neighbour: Element):
        if left_neighbour:
            left_neighbour.value += self.left.value
        if right_neighbour:
            right_neighbour.value += self.right.value
        self.element_type = "number"
        self.value = 0
        self.left = None
        self.right = None

    def find_large_number(self) -> Element | None:
        if self.element_type == "number":
            return self if self.value >= 10 else None

        left_large_number = self.left.find_large_number()
        if left_large_number:
            return left_large_number

        right_large_number = self.right.find_large_number()
        if right_large_number:
            return right_large_number

    def split(self):
        self.element_type = "pair"
        self.left = Element("number", value=math.floor(self.value / 2))
        self.right = Element("number", value=math.ceil(self.value / 2))
        self.left.up = (self, "left")
        self.right.up = (self, "right")
        self.value = None

    def reduce(self):
        fully_reduced = False
        while not fully_reduced:
            fully_reduced = True
            pair_to_explode, left_neighbour, right_neighbour = self.find_nested(4)
            if pair_to_explode:
                fully_reduced = False
                pair_to_explode.explode(left_neighbour, right_neighbour)
            else:
                pair_to_split = self.find_large_number()
                if pair_to_split:
                    fully_reduced = False
                    pair_to_split.split()

    def magnitude(self):
        if self.element_type == "pair":
            return 3 * self.left.magnitude() + 2 * self.right.magnitude()
        else:
            return self.value


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

element = elements[0]
for next_element in elements[1:]:
    element += next_element

print(element)
print(element.magnitude())

# only works if element reduction doesn't mutate
# num_elements = len(elements)
# pairwise_sum = [(elements[i] + elements[j]).magnitude() for i in range(num_elements) for j in range(num_elements) if i != j]

max_seen = 0
for i in range(len(puzzle_input)):
    for j in range(len(puzzle_input)):
        if i != j:
            x = create_element(puzzle_input[i])
            y = create_element(puzzle_input[j])
            curr = x + y
            max_seen = max(max_seen, curr.magnitude())

print(max_seen)
