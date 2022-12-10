import sys
from bisect import bisect_right


class Folder:
    def __init__(self, name, parent) -> None:
        self.name = name
        self.parent = parent
        self.children = list()
        self.size = None

    def populate_children(self, children):
        self.children = children

    def get_size(self):
        if self.size:
            return self.size
        else:
            self.size = 0
            for child in self.children.values():
                self.size += child.get_size()
            return self.size

    def __repr__(self) -> str:
        return f"{self.name} (dir) (children={list(self.children.values())})"


class File:
    def __init__(self, name, size) -> None:
        self.name = name
        self.size = size

    def get_size(self):
        return self.size

    def __repr__(self) -> str:
        return f"{self.name} (file, size={self.size})"


lines = open(sys.argv[1]).read().splitlines()
cursor = 0
root = Folder("/", None)
working_directory = None
all_directories = [root]

while cursor < len(lines):
    if "cd" in lines[cursor]:
        cd_what = lines[cursor].rsplit(" ", 1)[-1]
        if cd_what == "/":
            working_directory = root
        elif cd_what == "..":
            working_directory = working_directory.parent
        else:
            working_directory = working_directory.children[cd_what]
        cursor += 1
    else:  # ls
        children = dict()
        cursor += 1
        while not lines[cursor].startswith("$"):
            if "dir" in lines[cursor]:
                name = lines[cursor].split(" ")[-1]
                children[name] = Folder(name, working_directory)
                all_directories.append(children[name])
            else:
                size, name = lines[cursor].split(" ")
                children[name] = File(name, int(size))
            cursor += 1
            if cursor >= len(lines):
                break
        working_directory.populate_children(children)

directory_sizes = list(sorted((dir.get_size() for dir in all_directories)))

print(
    "Part 1:",
    sum(filter(lambda x: x < 100000, directory_sizes)),
)

unused_space = 70_000_000 - root.get_size()
target = 30_000_000 - unused_space

print("Part 2:", directory_sizes[bisect_right(directory_sizes, target)])
