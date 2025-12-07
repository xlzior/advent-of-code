from __future__ import annotations
from collections import namedtuple
from dataclasses import dataclass
from itertools import combinations
import math
import re


@dataclass
class Equipment:
    cost: int = 0
    damage: int = 0
    armour: int = 0

    def __add__(self, other: Equipment):
        return Equipment(
            self.cost + other.cost,
            self.damage + other.damage,
            self.armour + other.armour,
        )


Player = namedtuple("Player", ["hp", "damage", "armour"])


def parse_section(section):
    return [
        Equipment(*(int(x) for x in re.findall(r"  \d+", row)))
        for row in section.split("\n")[1:]
    ]


def run_simulation(a: Player, b: Player):
    a_damage_b = max(1, a.damage - b.armour)
    b_damage_a = max(1, b.damage - a.armour)
    a_rounds = math.ceil(a.hp / b_damage_a)
    b_rounds = math.ceil(b.hp / a_damage_b)
    return a if a_rounds >= b_rounds else b


weapons, armour, rings = "".join(open("shop.in", "r").readlines()).split("\n\n")
weapons = parse_section(weapons)
armour = parse_section(armour) + [Equipment()]
rings = parse_section(rings) + [Equipment(), Equipment()]

boss = Player(103, 9, 2)
min_cost_to_win = math.inf
max_cost_to_lose = 0
for w in weapons:
    for a in combinations(armour, 1):
        for r in combinations(rings, 2):
            all_equips = sum([w, *a, *r], Equipment())
            me = Player(100, all_equips.damage, all_equips.armour)
            if run_simulation(me, boss) == me:
                min_cost_to_win = min(min_cost_to_win, all_equips.cost)
            else:
                max_cost_to_lose = max(max_cost_to_lose, all_equips.cost)

print("Part 1:", min_cost_to_win)
print("Part 2:", max_cost_to_lose)
