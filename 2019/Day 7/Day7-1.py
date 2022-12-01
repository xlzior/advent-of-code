import sys
from itertools import permutations


class Computer:
    def __init__(self, program, inputs):
        self.program = program
        self.instruction_pointer = 0
        self.inputs = inputs
        self.input_pointer = 0
        self.operations = {
            1: self.add, 2: self.mul,
            3: self.write, 4: self.read,
            5: self.jump_if_true, 6: self.jump_if_false,
            7: self.less_than, 8: self.equals,
        }

    def next_int(self):
        result = self.program[self.instruction_pointer]
        self.instruction_pointer += 1
        return result

    def next_ints(self, n):
        return [self.next_int() for _ in range(n)]

    def next_op(self) -> tuple[list, int]:
        full_opcode = self.next_int()
        param_digits = full_opcode // 100
        param_modes = list()
        while param_digits > 0:
            param_modes.append(param_digits % 10)
            param_digits //= 10
        while len(param_modes) < 3:
            param_modes.append(0)
        return param_modes, full_opcode % 100

    def get_param(self, param, mode):
        return param if mode == 1 else self.program[param]

    def next_param(self, mode):
        return self.get_param(self.next_int(), mode)

    def next_params(self, n, modes):
        return [self.next_param(modes[i]) for i in range(n)]

    def next_input(self):
        result = self.inputs[self.input_pointer]
        self.input_pointer += 1
        return result

    def add(self, param_modes: list):
        r1, r2 = self.next_params(2, param_modes)
        w = self.next_int()
        self.program[w] = r1 + r2

    def mul(self, param_modes: list):
        r1, r2 = self.next_params(2, param_modes)
        w = self.next_int()
        self.program[w] = r1 * r2

    def write(self, param_modes: list):
        w = self.next_int()
        self.program[w] = self.next_input()

    def read(self, param_modes: list):
        return self.next_param(param_modes[0])

    def jump_if_true(self, param_modes: list):
        condition, jump_to = self.next_params(2, param_modes)
        if condition != 0:
            self.instruction_pointer = jump_to

    def jump_if_false(self, param_modes: list):
        condition, jump_to = self.next_params(2, param_modes)
        if condition == 0:
            self.instruction_pointer = jump_to

    def less_than(self, param_modes: list):
        r1, r2 = self.next_params(2, param_modes)
        w = self.next_int()
        self.program[w] = 1 if r1 < r2 else 0

    def equals(self, param_modes: list):
        r1, r2 = self.next_params(2, param_modes)
        w = self.next_int()
        self.program[w] = 1 if r1 == r2 else 0

    def run(self):
        result = None
        while True:
            param_modes, opcode = self.next_op()
            if opcode == 99:
                break

            result = self.operations[opcode](param_modes)
        return result


puzzle_input = list(map(int, open(sys.argv[1]).read().split(",")))

possible_outputs = list()
for phase_settings in permutations(range(5)):
    output = 0
    for i in range(5):
        computer = Computer(puzzle_input.copy(), [phase_settings[i], output])
        output = computer.run()

    possible_outputs.append(output)

print("Part 1:", max(possible_outputs))
