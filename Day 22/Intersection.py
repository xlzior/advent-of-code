from Cuboid import Cuboid

class Intersection:
    def __init__(self, original_cuboids: set[Cuboid], intersection: Cuboid):
        self.original_cuboids = original_cuboids
        self.ids = " ".join(map(str, sorted(map(lambda cuboid: cuboid.id, self.original_cuboids))))
        self.hash = hash(self.ids)
        self.intersection = intersection

    def __str__(self):
        return f"{self.ids} {self.size()}"

    def __eq__(self, other):
        return self.ids == other.ids

    def __hash__(self):
        return self.hash

    def intersect(self, other: Cuboid):
        if other not in self.original_cuboids:  # don't intersect something I've already intersected before
            new_original_cuboids = self.original_cuboids.copy()
            new_original_cuboids.add(other)
            new_intersection = self.intersection.intersect(other)
            if new_intersection:
                return Intersection(new_original_cuboids, new_intersection)

        # returns None if I've already intersected before, or if there's no intersection

    def size(self):
        return self.intersection.size()
