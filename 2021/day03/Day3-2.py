# numbers = [input() for i in range(12)]
numbers = [input() for i in range(1000)]
length_of_each_number = len(numbers[0])


def get_counts(lst, index):
    bits_at_index = [int(number[index]) for number in lst]
    count_1s = sum(bits_at_index)
    count_0s = len(lst) - count_1s
    return [count_0s, count_1s]


def most_common_bit(lst, index):
    [count_0s, count_1s] = get_counts(lst, index)
    return '1' if count_1s >= count_0s else '0'


def least_common_bit(lst, index):
    [count_0s, count_1s] = get_counts(lst, index)
    return '0' if count_1s >= count_0s else '1'


def find_rating(lst, bit_criteria_fn, index):
    while len(lst) > 1:
        bit_criteria = bit_criteria_fn(lst, index)
        lst = [num for num in lst if num[index] == bit_criteria]
        index += 1
    return lst[0]


oxygen = find_rating(numbers, most_common_bit, 0)
carbon_dioxide = find_rating(numbers, least_common_bit, 0)
print(f"oxygen: {oxygen}, carbon dioxide: {carbon_dioxide}")
print(f"oxygen: {int(oxygen, 2)}, carbon dioxide: {int(carbon_dioxide, 2)}")
print(f"life support: {int(oxygen, 2) * int(carbon_dioxide, 2)}")
