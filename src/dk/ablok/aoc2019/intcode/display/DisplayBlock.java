package dk.ablok.aoc2019.intcode.display;

public class DisplayBlock {
    // Values
    int x, y;
    Shape shape;
    Color front_color, back_color;

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
    public DisplayBlock(int x, int y, Shape s, Color front_color, Color back_color) {
        this.x = x;
        this.y = y;
        this.shape = s;
        this.front_color = front_color;
        this.back_color = back_color;
    }

    // Same, but for longs
    public DisplayBlock(long x, long y, Shape s, Color front_color, Color back_color) {
        this.x = Math.toIntExact(x);
        this.y = Math.toIntExact(y);
        this.shape = s;
        this.front_color = front_color;
        this.back_color = back_color;
    }

    // Constructor without background
    public DisplayBlock(int x, int y, Shape s, Color front_color) {
        this.x = x;
        this.y = y;
        this.shape = s;
        this.front_color = front_color;
    }

    // Same, but for longs
    public DisplayBlock(long x, long y, Shape s, Color front_color) {
        this.x = Math.toIntExact(x);
        this.y = Math.toIntExact(y);
        this.shape = s;
        this.front_color = front_color;
    }
}
