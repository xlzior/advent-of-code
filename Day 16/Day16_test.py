# from Day16 import parse_and_evaluate
from Day16_next import PacketParser

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

BLUE = '\033[94m'
GREEN = '\033[92m'
RED = '\033[91m'
ENDC = '\033[0m'

parser = PacketParser()

print(f"{BLUE}Part 1 Public Test Cases{ENDC}")
for sample, expected_version in part_1_tests:
    # observed_version, _ = parse_and_evaluate(sample)
    observed_version = parser.parse(sample).version_sum()
    if observed_version == expected_version:
        print(f"{GREEN}✓ {sample} -> version sum: {expected_version}{ENDC}")
    else:
        print(f"{RED}✗ {sample} version sum; Expected: {expected_version}, received {observed_version}{ENDC}")

print()
print(f"{BLUE}Part 2 Public Test Cases{ENDC}")
for sample, expected_value in part_2_tests:
    # _, observed_value = parse_and_evaluate(sample)
    observed_value = parser.parse(sample).eval()
    if observed_value == expected_value:
        print(f"{GREEN}✓ {sample} -> value: {expected_value}{ENDC}")
    else:
        print(f"{RED}✗ {sample} value; Expected: {expected_value}, received {observed_value}{ENDC}")
