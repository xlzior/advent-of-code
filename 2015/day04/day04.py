import hashlib

key = open("puzzle.txt").read()
md5_hash_starts_with_n_zeros = (
    lambda n: lambda x: hashlib.md5(bytes(f"{key}{x}", "utf-8"))
    .hexdigest()
    .startswith("0" * n)
)
part_1 = next(filter(md5_hash_starts_with_n_zeros(5), range(1, 1_000_000)))
print("Part 1:", part_1)

part_2 = next(filter(md5_hash_starts_with_n_zeros(6), range(part_1, 10_000_000)))
print("Part 2:", part_2)
