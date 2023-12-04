class Tape:
    def __init__(self, contents):
        self.contents = contents
        self.index = 0

    def __getitem__(self, item):
        return self.contents[item]

    def __setitem__(self, key, value):
        self.contents[key] = value

    def has_next(self):
        return self.index < len(self.contents)

    def next(self):
        result = self.contents[self.index]
        self.index += 1
        return result

    def extend(self, more_contents):
        self.contents.extend(more_contents)

    def rewind(self, n):
        self.index -= n

    def jump_to(self, n):
        self.index = n

class Computer:
    RUNNING = "RUNNING"
    PAUSED = "PAUSED"  # waiting for input
    HALTED = "HALTED"

    def __init__(self, program, inputs):
        self.program = Tape(program)
        self.inputs = Tape(inputs)
        self.output = None
        self.state = Computer.RUNNING
        self.operations = {
            1: self.add, 2: self.mul,
            3: self.write, 4: self.read,
            5: self.jump_if_true, 6: self.jump_if_false,
            7: self.less_than, 8: self.equals,
        }

    def next_ints(self, n):
        return [self.program.next() for _ in range(n)]

    def next_op(self) -> tuple[list, int]:
        full_opcode = self.program.next()
        param_digits = full_opcode // 100
        param_modes = list()
        while param_digits > 0:
            param_modes.append(param_digits % 10)
            param_digits //= 10
        param_modes.extend([0] * (3 - len(param_modes)))
        return param_modes, full_opcode % 100

    def next_param(self, mode):
        param = self.program.next()
        return param if mode == 1 else self.program[param]

    def next_params(self, n, modes):
        return [self.next_param(modes[i]) for i in range(n)]

    def resume(self, inputs):
        self.inputs.extend(inputs)
        return self.run()

    def add(self, param_modes: list):
        r1, r2 = self.next_params(2, param_modes)
        w = self.program.next()
        self.program[w] = r1 + r2

    def mul(self, param_modes: list):
        r1, r2 = self.next_params(2, param_modes)
        w = self.program.next()
        self.program[w] = r1 * r2

    def write(self, param_modes: list):
        if self.inputs.has_next():
            w = self.program.next()
            self.program[w] = self.inputs.next()
        else:
            self.state = Computer.PAUSED
            self.program.rewind(1)

    def read(self, param_modes: list):
        self.output = self.next_param(param_modes[0])

    def jump_if_true(self, param_modes: list):
        condition, destination = self.next_params(2, param_modes)
        if condition != 0:
            self.program.jump_to(destination)

    def jump_if_false(self, param_modes: list):
        condition, destination = self.next_params(2, param_modes)
        if condition == 0:
            self.program.jump_to(destination)

    def less_than(self, param_modes: list):
        r1, r2 = self.next_params(2, param_modes)
        w = self.program.next()
        self.program[w] = 1 if r1 < r2 else 0

    def equals(self, param_modes: list):
        r1, r2 = self.next_params(2, param_modes)
        w = self.program.next()
        self.program[w] = 1 if r1 == r2 else 0

    def run(self):
        self.output = None
        self.state = Computer.RUNNING
        while self.state == Computer.RUNNING:
            param_modes, opcode = self.next_op()
            if opcode == 99:
                self.state = Computer.HALTED
            else:
                self.operations[opcode](param_modes)

        return self.output
