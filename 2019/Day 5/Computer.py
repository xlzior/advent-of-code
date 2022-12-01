class Computer:
    def __init__(self, program):
        self.program = program
        self.instruction_pointer = 0
        self.operations = {
            1: self.add, 2: self.mul,
            3: self.write, 4: self.read,
            5: self.jump_if_true, 6: self.jump_if_false,
            7: self.less_than, 8: self.equals,
            99: self.end
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
        self.program[w] = int(input())

    def read(self, param_modes: list):
        print(self.next_param(param_modes[0]))

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

    def end(self, param_modes: list):
        return True

    def run(self):
        halted = False
        while not halted:
            param_modes, opcode = self.next_op()
            if opcode not in self.operations:
                print(f"Unknown opcode: {opcode}")
                break
            else:
                halted = self.operations[opcode](param_modes)
