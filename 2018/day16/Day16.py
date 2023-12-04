import sys


class Simulator:
    def __init__(self):
        self.commands = ["addr", "addi", "mulr", "muli", "banr", "bani", "borr", "bori",
                         "setr", "seti", "gtir", "gtri", "gtrr", "eqir", "eqri", "eqrr"]
        self.possible_commands = [[op, set(self.commands)] for op in range(16)]
        self.commands_by_opcode = [""] * 16

        self.registers = [0, 0, 0, 0]

    def extract_array(self, raw_string: str):
        if "[" in raw_string:
            start = raw_string.index("[")
            end = raw_string.index("]")
            return list(map(int, raw_string[start + 1:end].split(", ")))
        return raw_string

    def deduce_possible_opcodes(self, observations):
        count = 0

        for observation in observations:
            [before, command, after] = list(map(self.extract_array, observation.split("\n")))
            possible_opcodes = self.deduce_one_set(before, command, after)

            if possible_opcodes >= 3:
                count += 1

        return count

    def deduce_one_set(self, before, command, after):
        [op, a, b, c] = list(map(int, command.split(" ")))  # at this stage, we don't know what op means
        possible_commands = set(command for command in self.commands
                                if self.run_command(before, command, a, b, c) == after)
        self.possible_commands[op][1].intersection_update(possible_commands)
        return len(possible_commands)

    def match_opcodes_to_commands(self):
        if len(self.possible_commands) == 0:
            return

        self.possible_commands.sort(key=lambda x: -len(x[1]))  # those with only 1 opcode are shifted to the back
        [op, matching_command] = self.possible_commands.pop()
        assert len(matching_command) == 1  # if this assertion fails, then there is insufficient information
        matching_command = matching_command.pop()
        self.commands_by_opcode[op] = matching_command

        for entry in self.possible_commands:
            if matching_command in entry[1]:
                entry[1].remove(matching_command)

        self.match_opcodes_to_commands()

    def run_command(self, registers, command_word, a, b, c):
        ra = registers[a]
        rb = registers[b]

        results = {
            "addr": ra + rb, "addi": ra + b, "mulr": ra * rb, "muli": ra * b,
            "banr": ra & rb, "bani": ra & b, "borr": ra | rb, "bori": ra | b,
            "setr": ra,      "seti": a,
            "gtir": 1 if a > rb else 0,  "gtri": 1 if ra > b else 0,  "gtrr": 1 if ra > rb else 0,
            "eqir": 1 if a == rb else 0, "eqri": 1 if ra == b else 0, "eqrr": 1 if ra == rb else 0,
        }

        copy = registers.copy()
        copy[c] = results[command_word]
        return copy

    def run_op(self, op, a, b, c):
        self.registers = self.run_command(self.registers, self.commands_by_opcode[op], a, b, c)


with open(sys.argv[1]) as file:
    lines = file.read()
    observations, test_program = lines.split("\n\n\n\n")
    observations = observations.split("\n\n")
    test_program = test_program.split("\n")

simulator = Simulator()
count = simulator.deduce_possible_opcodes(observations)
print("Part 1:", count)

simulator.match_opcodes_to_commands()

registers = [0, 0, 0, 0]
for command in test_program:
    [op, a, b, c] = list(map(int, command.strip().split(" ")))
    simulator.run_op(op, a, b, c)
print("Part 2:", simulator.registers)
