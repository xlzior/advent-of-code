import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

class Cuboid {
    int startX;
    int endX;
    int startY;
    int endY;
    int startZ;
    int endZ;
    boolean isOn;

    Cuboid(int startX, int endX, int startY, int endY, int startZ, int endZ, boolean isOn) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.startZ = startZ;
        this.endZ = endZ;
        this.isOn = isOn;
    }

    public Cuboid intersect(Cuboid other) {
        int startX = Math.max(this.startX, other.startX);
        int startY = Math.max(this.startY, other.startY);
        int startZ = Math.max(this.startZ, other.startZ);
        int endX = Math.min(this.endX, other.endX);
        int endY = Math.min(this.endY, other.endY);
        int endZ = Math.min(this.endZ, other.endZ);

        if (startX <= endX && startY <= endY && startZ <= endZ) {
            return new Cuboid(startX, endX, startY, endY, startZ, endZ, !other.isOn);
        }
        return null;
    }

    public long size() {
        return (long) (this.isOn ? 1 : -1) * (this.endX - this.startX + 1) *
                (this.endY - this.startY + 1) * (this.endZ - this.startZ + 1);
    }
}

public class Day22 {
    ArrayList<Cuboid> originals = new ArrayList<>();
    ArrayList<Cuboid> cuboids = new ArrayList<>();

    public void run() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String[] input = sc.nextLine().split(" x=|,y=|,z=|\\.\\.");
            int[] coords = IntStream.range(1, 7).map(i -> Integer.parseInt(input[i])).toArray();
            String state = input[0];
            originals.add(new Cuboid(coords[0], coords[1], coords[2],
                    coords[3], coords[4], coords[5], state.equals("on")));
        }

        long count = 0;
        for (Cuboid original : originals) {
            for (Cuboid cuboid : new ArrayList<>(cuboids)) {
                Cuboid intersection = original.intersect(cuboid);
                if (intersection != null) {
                    cuboids.add(intersection);
                    count += intersection.size();
                }
            }
            if (original.isOn) {
                cuboids.add(original);
                count += original.size();
            }
        }
        System.out.println(count);
    }

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        Day22 runner = new Day22();
        runner.run();
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }
}
