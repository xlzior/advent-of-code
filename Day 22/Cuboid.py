from __future__ import annotations  # for self-referential type annotations

class Cuboid:
    def __init__(self, id, start_x, end_x, start_y, end_y, start_z, end_z):
        self.id = id
        self.start_x = start_x
        self.end_x = end_x
        self.start_y = start_y
        self.end_y = end_y
        self.start_z = start_z
        self.end_z = end_z

    def __eq__(self, other):
        return self.id == other.id

    def __hash__(self):
        return hash(self.id)

    def intersect(self, other: Cuboid):
        start_x = max(self.start_x, other.start_x)
        start_y = max(self.start_y, other.start_y)
        start_z = max(self.start_z, other.start_z)
        end_x = min(self.end_x, other.end_x)
        end_y = min(self.end_y, other.end_y)
        end_z = min(self.end_z, other.end_z)

        if start_x <= end_x and start_y <= end_y and start_z <= end_z:
            return Cuboid(-1, start_x, end_x, start_y, end_y, start_z, end_z)

    def size(self):
        return (self.end_x - self.start_x + 1) * (self.end_y - self.start_y + 1) * (self.end_z - self.start_z + 1)
