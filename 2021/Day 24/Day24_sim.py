import math
import sys

class Computer:
    def __init__(self):
        self.variables = {"w": 0, "x": 0, "y": 0, "z": 0}
        self.index = 0

    def reset(self):
        self.variables = {"w": 0, "x": 0, "y": 0, "z": 0}

    def get_value(self, string):
        return self.variables[string] if string in self.variables else int(string)

    def get_input(self):
        result = self.inputs[self.index]
        self.index += 1
        return result

        # return input()  # for testing

    def run(self, raw_command):
        command, params = raw_command.split(" ", maxsplit=1)
        if command == "inp":
            self.variables[params] = int(self.get_input())
            return

        a, b = params.split()
        if command == "add":
            self.variables[a] = self.get_value(a) + self.get_value(b)
        elif command == "mul":
            self.variables[a] = self.get_value(a) * self.get_value(b)
        elif command == "div":
            self.variables[a] = math.trunc(self.get_value(a) / self.get_value(b))
        elif command == "mod":
            self.variables[a] = self.get_value(a) % self.get_value(b)
        elif command == "eql":
            self.variables[a] = 1 if self.get_value(a) == self.get_value(b) else 0

    def run_all(self, raw_commands, inputs):
        self.reset()
        self.inputs = inputs
        self.index = 0
        for raw_command in raw_commands:
            self.run(raw_command)

        return self.variables


puzzle_input = open(sys.argv[1]).read().split("\n")

computer = Computer()
print(computer.run_all(puzzle_input, "13579246899999"))
