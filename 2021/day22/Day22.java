import java.util.ArrayList;
import java.util.Scanner;

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
            String state = input[0];
            int startX = Integer.parseInt(input[1]);
            int endX = Integer.parseInt(input[2]);
            int startY = Integer.parseInt(input[3]);
            int endY = Integer.parseInt(input[4]);
            int startZ = Integer.parseInt(input[5]);
            int endZ = Integer.parseInt(input[6]);
            originals.add(new Cuboid(startX, endX, startY, endY, startZ, endZ, state.equals("on")));
        }

        long count = 0;
        for (Cuboid original : originals) {
            ArrayList<Cuboid> newCuboids = new ArrayList<>();
            for (Cuboid cuboid : cuboids) {
                Cuboid intersection = original.intersect(cuboid);
                if (intersection != null) {
                    newCuboids.add(intersection);
                    count += intersection.size();
                }
            }
            cuboids.addAll(newCuboids);
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
