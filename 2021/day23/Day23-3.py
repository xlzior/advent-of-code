import collections
import queue
import sys
import copy

NUM_ROOMS = 4
EMPTY = "."
possible_hall_spots = [0, 1, 3, 5, 7, 9, 10]        # 2 4 6 8 are outside a room and are invalid stopping points
room_positions = {"A": 2, "B": 4, "C": 6, "D": 8}
costs = {"A": 1, "B": 10, "C": 100, "D": 1000}

class State:
    def __init__(self, rooms: dict, hall: str, energy: int):
        self.rooms = rooms
        self.hall = hall
        self.energy = energy
        self.string = self.generate_string()
        self.score = self.calculate_score()

    def __str__(self):
        return self.string

    def __hash__(self):
        return hash(self.string)

    def __eq__(self, other):
        return self.string == other.string

    def __lt__(self, other):  # in the PQ, prioritise states which are closer to completion
        return self.score > other.score

    def get_nth_amphipod(self, n):
        return lambda room: room[n] if n < len(room) else EMPTY

    def generate_string(self):
        lines = [
            f"#############",
            f"#{self.hall}#",
        ]

        first = True
        for n in range(ROOM_SIZE - 1, -1, -1):
            a, b, c, d = map(self.get_nth_amphipod(n), self.rooms.values())
            line = f"#{a}#{b}#{c}#{d}#"
            if first:
                line = f"##{line}##"
                first = False
            else:
                line = f"  {line}  "
            lines.append(line)

        lines.append("  #########  ")
        return "\n".join(lines)

    def calculate_score(self):  # number of amphipods which are correct and don't need moving
        num_correct = 0
        for letter in self.rooms:
            room = self.rooms[letter]
            for amphipod in room:
                if amphipod == letter:
                    num_correct += 1
                else:
                    break
        return num_correct

    def is_hall_empty(self, start, end):
        if start > end:
            start, end = end, start
        return all([char == EMPTY for char in self.hall[start:end + 1]])

    def can_move_in(self, amphipod):
        return all(map(lambda cell: cell == amphipod, self.rooms[amphipod]))

    def simulate_room_to_hall(self):
        room_to_hall = list()

        for room in self.rooms:
            if len(self.rooms[room]) == 0:  # room is empty
                continue
            amphipod = self.rooms[room][-1]  # move the top amphipod in the room
            start = room_positions[room]
            for end in possible_hall_spots:
                if self.is_hall_empty(start, end):
                    new_rooms = copy.deepcopy(self.rooms)
                    new_rooms[room].pop()
                    new_hall = "".join(self.hall[i] if i != end else amphipod for i in range(len(self.hall)))
                    steps_taken = (ROOM_SIZE - len(self.rooms[room])) + abs(end - start) + 1

                    new_state = State(new_rooms, new_hall, self.energy + steps_taken * costs[amphipod])
                    if new_state.score >= self.score:  # don't move correct amphipods out
                        room_to_hall.append(new_state)

        return room_to_hall

    def simulate_hall_to_room(self):
        hall_to_room = list()

        for pos in range(len(self.hall)):
            amphipod = self.hall[pos]
            if amphipod == EMPTY:  # hall is empty
                continue
            start = pos
            end = room_positions[amphipod]
            start += (1 if start < end else -1)  # exclude the start from the range
            if self.can_move_in(amphipod) and self.is_hall_empty(start, end):
                new_rooms = copy.deepcopy(self.rooms)
                new_rooms[amphipod].append(amphipod)
                new_hall = "".join(self.hall[i] if i != pos else EMPTY for i in range(len(self.hall)))
                steps_taken = (ROOM_SIZE - len(new_rooms[amphipod])) + abs(end - pos) + 1

                new_state = State(new_rooms, new_hall, self.energy + steps_taken * costs[amphipod])
                hall_to_room.append(new_state)

        return hall_to_room

    def simulate_all(self):
        if self.score < ROOM_SIZE * NUM_ROOMS:
            return self.simulate_room_to_hall() + self.simulate_hall_to_room()


puzzle_input = open(sys.argv[1]).read().strip().split("\n")
ROOM_SIZE = len(puzzle_input) - 3

initial_hall = puzzle_input[1][1:-1]
initial_rooms = {
    "A": [puzzle_input[i][3] for i in range(ROOM_SIZE + 1, 1, -1)],
    "B": [puzzle_input[i][5] for i in range(ROOM_SIZE + 1, 1, -1)],
    "C": [puzzle_input[i][7] for i in range(ROOM_SIZE + 1, 1, -1)],
    "D": [puzzle_input[i][9] for i in range(ROOM_SIZE + 1, 1, -1)]
}
print(initial_rooms)

# used for reading in intermediate states where some amphipods are in the halls
for room in initial_rooms:
    while len(initial_rooms[room]) > 0 and initial_rooms[room][-1] == EMPTY:
        initial_rooms[room].pop()

initial_state = State(initial_rooms, initial_hall, 0)
print(initial_state)

shortest_paths = collections.defaultdict(lambda: 99999999999999)
visited = set()

pq = queue.PriorityQueue()
pq.put((0, initial_state))

while not pq.empty():
    distance, current_state = pq.get()

    visited.add(current_state)
    shortest_paths[current_state] = distance
    if current_state.score == ROOM_SIZE * NUM_ROOMS:  # terminate early if I finalised something that's done
        print(current_state.energy)
        break

    for neighbour in current_state.simulate_all():
        if neighbour not in visited:
            old_cost = shortest_paths[neighbour]
            new_cost = neighbour.energy
            if new_cost < old_cost:
                pq.put((new_cost, neighbour))
                shortest_paths[neighbour] = new_cost
