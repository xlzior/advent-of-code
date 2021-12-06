import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Point implements Comparable<Point> {
    int x;
    int y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point that) {
        int xComparator = this.x - that.x;
        return xComparator == 0 ? this.y - that.y : xComparator;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }

    public static Point minimum(Point a, Point b) {
        
    }
}

class Line implements Comparable<Line> {
    Point start;
    Point end;

    Line(String representation) {
        String regex = "(?<startX>\\d+),(?<startY>\\d+) -> (?<endX>\\d+),(?<endY>\\d+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(representation);
        while (matcher.find()) {
            Point start = new Point(
                    Integer.parseInt(matcher.group("startX")),
                    Integer.parseInt(matcher.group("startY")));
            Point end = new Point(
                    Integer.parseInt(matcher.group("endX")),
                    Integer.parseInt(matcher.group("endY")));
            if (start.x > end.x) {
                this.start = end;
                this.end = start;
            } else {
                this.start = start;
                this.end = end;
            }
        }
    }

    boolean isHorizontal() {
        return this.start.y == this.end.y;
    }

    boolean isVertical() {
        return this.start.x == this.end.x;
    }

    int length() {
        return (int) Math.sqrt(
                Math.pow(this.start.x - this.end.x, 2) +
                Math.pow(this.start.y - this.end.y, 2));
    }

    @Override
    public int compareTo(Line that) {
        int startComparator = this.start.compareTo(that.start);
        return startComparator == 0 ? this.end.compareTo(that.end) : startComparator;
    }

    @Override
    public String toString() {
        return String.format("%s to %s", this.start, this.end);
    }
}

class RangeManager {
    TreeSet<Line> ranges = new TreeSet<>();
    void add(Line newRange) {
        Line closest = ranges.floor(newRange);
    }

    int getCount() {
        int count = 0;
        for (Line line : ranges) {
            count += line.length();
        }
        return count;
    }
}

public class Day5 {
    RangeManager overlappingRanges = new RangeManager();
    ArrayList<Line> horizontalLines = new ArrayList<>();
    ArrayList<Line> verticalLines = new ArrayList<>();

    void readInput() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String rawLine = sc.nextLine();
            Line line = new Line(rawLine);
            if (line.isHorizontal()) {
                this.horizontalLines.add(line);
            } else if (line.isVertical()) {
                this.verticalLines.add(line);
            }
        }
        Collections.sort(this.horizontalLines);
        Collections.sort(this.verticalLines);
    }

    void findHorizontalOverlaps() {
        for (int i = 1; i < this.horizontalLines.size(); i++) {
            Line previous = this.horizontalLines.get(i - 1);
            Line curr = this.horizontalLines.get(i);
            if (previous.end.x < curr.start.x) {
                continue; // no overlap
            }

            overlappingRanges.add(new Line(
                    curr.start,

            ));
        }
    }

    void findVerticalOverlaps() {

    }

    void findPerpendicularOverlaps() {

    }

    void run() {
        readInput();
        findHorizontalOverlaps();
        findVerticalOverlaps();
        findPerpendicularOverlaps();
        System.out.println(overlappingRanges.getCount());
    }

    public static void main(String[] args) {
        Day5 runner = new Day5();
        runner.run();
    }
}
