# Inspired by: https://github.com/teekaytai/advent-of-code-2021/blob/main/Day%2016/Day16.java

from abc import ABC, abstractmethod
import functools


class Packet(ABC):
    def __init__(self, version, type_id):
        self.version = version
        self.type_id = type_id

    @abstractmethod
    def version_sum(self):
        pass

    @abstractmethod
    def eval(self):
        pass


class LiteralPacket(Packet):
    def __init__(self, version, type_id, value):
        super().__init__(version, type_id)
        self.value = value

    def __str__(self):
        return f"(literal: {self.version}, {self.type_id}, {self.value})"

    def version_sum(self):
        return self.version

    def eval(self):
        return self.value


class OperatorPacket(Packet):
    def __init__(self, version, type_id, subpackets):
        super().__init__(version, type_id)
        self.subpackets = subpackets

    def __str__(self):
        return f"(operator: {self.version}, {self.type_id}, [{', '.join(map(str, self.subpackets))}])"

    def version_sum(self):
        return self.version + sum(map(lambda subpacket: subpacket.version_sum(), self.subpackets))

    def eval(self):
        subpacket_values = map(lambda subpacket: subpacket.eval(), self.subpackets)
        if self.type_id == 0:
            return sum(subpacket_values)
        elif self.type_id == 1:
            return functools.reduce(lambda x, y: x * y, subpacket_values)
        elif self.type_id == 2:
            return min(subpacket_values)
        elif self.type_id == 3:
            return max(subpacket_values)

        first, second = subpacket_values  # binary operators
        if self.type_id == 5:
            return 1 if first > second else 0
        elif self.type_id == 6:
            return 1 if first < second else 0
        elif self.type_id == 7:
            return 1 if first == second else 0


class PacketParser:
    def next_n_bits(self, n):
        result = self.binary_repr[self.index:self.index+n]
        self.index += n
        return result

    def next_bit(self):
        return self.next_n_bits(1)

    def next_header(self):
        version = int(self.next_n_bits(3), 2)  # the first three bits encode the packet version
        type_id = int(self.next_n_bits(3), 2)  # the next three bits encode the packet type ID
        return version, type_id

    def next_literal(self):
        value = list()
        while self.next_bit() == "1":           # a literal value comprises 1 bit to denote whether to continue reading
            value.append(self.next_n_bits(4))   # and 4 bits of data
        value.append(self.next_n_bits(4))       # the last group of 5 bits starts with a "0", but should still be read
        return int("".join(value), 2)

    def next_subpackets(self):
        length_type = self.next_bit()
        subpackets = list()
        if length_type == "0":
            subpacket_length = int(self.next_n_bits(15), 2)     # next 15 bits are a number that represents
            end_of_subpackets = self.index + subpacket_length   # the total length in bits of the sub-packets
            while self.index < end_of_subpackets:
                subpackets.append(self.next_packet())
        else:
            num_subpackets = int(self.next_n_bits(11), 2)       # next 11 bits are a number that represents
            for i in range(num_subpackets):                     # the number of sub-packets immediately contained
                subpackets.append(self.next_packet())
        return subpackets

    def next_packet(self):
        version, type_id = self.next_header()
        if type_id == 4:
            return LiteralPacket(version, type_id, self.next_literal())
        else:
            return OperatorPacket(version, type_id, self.next_subpackets())

    def parse(self, hexadecimal_repr):
        binary_repr = bin(int(hexadecimal_repr, 16))[2:]
        self.binary_repr = binary_repr.zfill(4 * len(hexadecimal_repr))  # pad 0s to achieve the intended number of bits
        self.index = 0
        return self.next_packet()


with open("input.txt") as file:
    puzzle_input = file.read().strip()

packet = PacketParser().parse(puzzle_input)
print(packet.version_sum(), packet.eval())
