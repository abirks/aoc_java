package dk.ablok.aoc2019.intcode.display;

// TODO implement a custom interface instead
public class DisplayBlock {
    // Values
    int x;
    int y;
    Shape shape;
    Color frontColor;
    Color backColor;

    // Shape constants
    public enum Shape {
        RECTANGLE,
        EMPTY_RECTANGLE,
        CIRCLE,
        ARROW_UP,
        ARROW_RIGHT,
        ARROW_DOWN,
        ARROW_LEFT
    }

    // Color constants
    public enum Color {
        BLACK,
        BLUE,
        GREEN,
        CYAN,
        RED,
        PURPLE,
        YELLOW,
        WHITE
    }

    // Constructor with background
    public DisplayBlock(int x, int y, Shape s, Color frontColor, Color backColor) {
        this.x = x;
        this.y = y;
        this.shape = s;
        this.frontColor = frontColor;
        this.backColor = backColor;
    }

    // Same, but for longs
    public DisplayBlock(long x, long y, Shape s, Color frontColor, Color backColor) {
        this.x = Math.toIntExact(x);
        this.y = Math.toIntExact(y);
        this.shape = s;
        this.frontColor = frontColor;
        this.backColor = backColor;
    }

    // Constructor without background
    public DisplayBlock(int x, int y, Shape s, Color frontColor) {
        this.x = x;
        this.y = y;
        this.shape = s;
        this.frontColor = frontColor;
    }

    // Same, but for longs
    public DisplayBlock(long x, long y, Shape s, Color frontColor) {
        this.x = Math.toIntExact(x);
        this.y = Math.toIntExact(y);
        this.shape = s;
        this.frontColor = frontColor;
    }
}
