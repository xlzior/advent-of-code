class Computer:
    def __init__(self, program):
        self.program = program
        self.program_counter = 0

    def next_int(self):
        result = self.program[self.program_counter]
        self.program_counter += 1
        return result

    def run(self):
        while True:
            opcode = self.next_int()
            if opcode == 1:
                read_index_1 = self.next_int()
                read_index_2 = self.next_int()
                write_index = self.next_int()
                self.program[write_index] = self.program[read_index_1] + self.program[read_index_2]
            elif opcode == 2:
                read_index_1 = self.next_int()
                read_index_2 = self.next_int()
                write_index = self.next_int()
                self.program[write_index] = self.program[read_index_1] * self.program[read_index_2]
            elif opcode == 99:
                return
            else:
                print(f"Unknown opcode: {opcode}")
                assert False
