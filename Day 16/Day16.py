from abc import ABC, abstractmethod
import functools


class Packet(ABC):
    def __init__(self, version, type_id):
        self.version = version
        self.type_id = type_id

    @abstractmethod
    def get_version_sum(self):
        pass

    @abstractmethod
    def eval(self):
        pass


class LiteralPacket(Packet):
    def __init__(self, version, type_id, value):
        Packet.__init__(self, version, type_id)
        self.value = value

    def __str__(self):
        return f"(literal: {self.version}, {self.type_id}, {self.value})"

    def get_version_sum(self):
        return self.version

    def eval(self):
        return self.value


class OperatorPacket(Packet):
    def __init__(self, version, type_id, subpackets):
        Packet.__init__(self, version, type_id)
        self.subpackets = subpackets

    def __str__(self):
        return f"(operator: {self.version}, {self.type_id}, [{', '.join(map(str, self.subpackets))}])"

    def get_version_sum(self):
        return self.version + sum(map(lambda sub: sub.get_version_sum(), self.subpackets))

    def eval(self):
        subpacket_values = map(lambda packet: packet.eval(), self.subpackets)
        if self.type_id == 0:
            return sum(subpacket_values)
        elif self.type_id == 1:
            return functools.reduce(lambda x, y: x * y, subpacket_values)
        elif self.type_id == 2:
            return min(subpacket_values)
        elif self.type_id == 3:
            return max(subpacket_values)

        # binary operators
        first, second = subpacket_values
        if self.type_id == 5:
            return 1 if first > second else 0
        elif self.type_id == 6:
            return 1 if first < second else 0
        elif self.type_id == 7:
            return 1 if first == second else 0


def parse_first_packet(binary_repr):
    """Parse the first packet in a given binary representation"""
    if len(binary_repr) < 6:
        return None, 0

    version = int(binary_repr[0:3], 2)
    type_id = int(binary_repr[3:6], 2)

    if type_id == 4:
        value, until = parse_literal_value(binary_repr[6:])
        return LiteralPacket(version, type_id, value), 6 + until
    else:
        length_type_id = binary_repr[6]
        if length_type_id == "0":
            total_length_of_sub = int(binary_repr[7:22], 2)
            subpackets, until = parse_packets(binary_repr[22:22 + total_length_of_sub])
            return OperatorPacket(version, type_id, subpackets), 22 + until
        else:
            number_of_sub = int(binary_repr[7:18], 2)
            subpackets, until = parse_n_packets(binary_repr[18:], number_of_sub)
            return OperatorPacket(version, type_id, subpackets), 18 + until


def parse_packets(binary_repr):
    """Parse all the packets in a given binary representation"""
    results = list()
    packet, until = parse_first_packet(binary_repr)
    combined_until = until
    while packet:
        results.append(packet)
        binary_repr = binary_repr[until:]
        packet, until = parse_first_packet(binary_repr)
        combined_until += until

    return results, combined_until


def parse_n_packets(binary_repr, n):
    """Parse the first n (back-to-back) packets in a given binary representation"""
    results = list()
    combined_until = 0
    for i in range(n):
        packet, until = parse_first_packet(binary_repr)
        results.append(packet)
        binary_repr = binary_repr[until:]
        combined_until += until

    return results, combined_until


def parse_literal_value(binary_repr):
    """Parse a literal value.

    A literal value comprises 1 or more 5-bit groups (1-bit `continue_reading` flag and 4-bit data).
    """
    i = 0
    continue_reading = True
    data = []
    while continue_reading:
        continue_reading = binary_repr[i] == "1"
        data.append(binary_repr[i+1:i+5])
        i += 5
    return int("".join(data), 2), i


def parse_and_evaluate(hexadecimal_repr):
    """Parse a single packet in its hexadecimal representation and return the answers to Part 1 and Part 2"""
    binary_repr = bin(int(hexadecimal_repr, 16))[2:]
    binary_repr = binary_repr.zfill(4 * len(hexadecimal_repr))  # pad 0s to achieve the intended number of bits
    packet, until = parse_first_packet(binary_repr)

    version_sum = packet.get_version_sum()
    value = packet.eval()
    return version_sum, value


# public tests cases
part_1_tests = [
    ("8A004A801A8002F478", 16),
    ("620080001611562C8802118E34", 12),
    ("C0015000016115A2E0802F182340", 23),
    ("A0016C880162017C3686B18A3D4780", 31)
]

part_2_tests = [
    ("C200B40A82", 3),
    ("04005AC33890", 54),
    ("880086C3E88112", 7),
    ("CE00C43D881120", 9),
    ("D8005AC2A8F0", 1),
    ("F600BC2D8F", 0),
    ("9C005AC2F8F0", 0),
    ("9C0141080250320F1802104A08", 1)
]

HEADER = '\033[94m'
GREEN = '\033[92m'
RED = '\033[91m'
ENDC = '\033[0m'

print(f"{HEADER}Part 1 Public Test Cases{ENDC}")
for sample, expected_version in part_1_tests:
    observed_version, _ = parse_and_evaluate(sample)
    if observed_version == expected_version:
        print(f"{GREEN}✓ {sample} -> version sum: {expected_version}{ENDC}")
    else:
        print(f"{RED}✗ {sample} version sum; Expected: {expected_version}, received {observed_version}{ENDC}")

print()
print(f"{HEADER}Part 2 Public Test Cases{ENDC}")
for sample, expected_value in part_2_tests:
    _, observed_value = parse_and_evaluate(sample)
    if observed_value == expected_value:
        print(f"{GREEN}✓ {sample} -> value: {expected_value}{ENDC}")
    else:
        print(f"{RED}✗ {sample} value; Expected: {expected_value}, received {observed_value}{ENDC}")


with open("input.txt") as file:
    puzzle_input = file.read().strip()

print()
print(parse_and_evaluate(puzzle_input))
