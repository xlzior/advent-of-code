# numbers = [input() for i in range(12)]
numbers = [input() for i in range(1000)]
length_of_each_number = len(numbers[0])


def most_common_bit(index):
    bits_at_index = [int(number[index]) for number in numbers]
    return '1' if sum(bits_at_index) > (len(numbers) / 2) else '0'


def get_gamma():
    mcbs = [most_common_bit(i) for i in range(length_of_each_number)]
    return "".join(mcbs)


def ones_complement(binary_number):
    complemented = [str(1 - int(bit)) for bit in binary_number]
    return "".join(complemented)


gamma = get_gamma()
epsilon = ones_complement(gamma)
print(f"gamma: {gamma}, epsilon: {epsilon}")
print(f"gamma: {int(gamma, 2)}, epsilon: {int(epsilon, 2)}")
print(f"power consumption: {int(gamma, 2) * int(epsilon, 2)}")
