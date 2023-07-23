package TexWriter.General;
import java.awt.Point;


public class Coordinates extends Point {
    public Coordinates(int x, int y) {
        super(x, y);
    }
    public Coordinates(Point p) {
        super(p);
    }
    Point left() {
        return new Point(x - 1, y);
    }
    Point right() {
        return new Point(x + 1, y);
    }
    Point up() {
        return new Point(x, y - 1);
    }
    Point down() {
        return new Point(x, y + 1);
    }
}

