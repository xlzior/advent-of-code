import sys

with open(sys.argv[1]) as file:
    boxes = [line.strip() for line in file.readlines()]

all_pairs = [[boxes[i], boxes[j]] for i in range(len(boxes)) for j in range(i, len(boxes))]

for box_1, box_2 in all_pairs:
    index = None
    for i in range(len(box_1)):
        if box_1[i] != box_2[i]:
            if index:  # more than one different character; move on to next pair
                index = None
                break
            index = i

    if index:
        print("Box 1:", box_1)
        print("Box 2:", box_2)
        print("Common letters:", box_1[:index] + box_1[index + 1:])
        break
