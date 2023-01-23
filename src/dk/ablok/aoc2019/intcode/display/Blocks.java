package dk.ablok.aoc2019.intcode.display;

import javax.swing.*;
import java.awt.*;

class Blocks extends JComponent {
    int res_x;
    int res_y;
    int size_x;
    int size_y;
    DisplayBlock.Shape[][] shapes;
    DisplayBlock.Color[][] fronts, backs;
    DisplayBlock.Color def_color;

    public Blocks(int res_x, int res_y, int size_x, int size_y, DisplayBlock.Shape defaultShape, DisplayBlock.Color defaultColor) {
        this.res_x = res_x;
        this.res_y = res_y;
        this.size_x = size_x;
        this.size_y = size_y;

        def_color = defaultColor;

        fronts = new DisplayBlock.Color[res_x][res_y];
        backs = new DisplayBlock.Color[res_x][res_y];
        shapes = new DisplayBlock.Shape[res_x][res_y];

        for (int y = 0; y < res_y; y++) {
            for (int x = 0; x < res_x; x++) {
                shapes[x][y] = defaultShape;
                fronts[x][y] = defaultColor;
                backs[x][y] = defaultColor;
            }
        }
    }

    public void change(int x, int y, DisplayBlock.Shape shape, DisplayBlock.Color front_color, DisplayBlock.Color back_color) {
        if (0 <= x && x < res_x && 0 <= y && y < res_y) {
            shapes[x][y] = shape;
            fronts[x][y] = front_color;
            backs[x][y] = back_color;
        }
    }

    public void setColor(Graphics g, DisplayBlock.Color c) {
        switch (c) {
            case BLACK -> g.setColor(new Color(0, 0, 0));
            case BLUE -> g.setColor(new Color(0, 0, 255));
            case GREEN -> g.setColor(new Color(0, 255, 0));
            case CYAN -> g.setColor(new Color(0, 255, 255));
            case RED -> g.setColor(new Color(255, 0, 0));
            case PURPLE -> g.setColor(new Color(255, 0, 255));
            case YELLOW -> g.setColor(new Color(255, 255, 0));
            case WHITE -> g.setColor(new Color(255, 255, 255));
            default -> g.setColor(new Color(127, 127, 127));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int y = 0; y < res_y; y++) {
            for (int x = 0; x < res_x; x++) {
                // Set shape and color
                switch (shapes[x][y]) {
                    case RECTANGLE -> {
                        setColor(g, fronts[x][y]);
                        g.fillRect(x * size_x, y * size_y, size_x, size_y);
                    }
                    case EMPTY_RECTANGLE -> {
                        setColor(g, fronts[x][y]);
                        g.fillRect(x * size_x, y * size_y, size_x, size_y);
                        setColor(g, backs[x][y]);
                        g.fillRect(x * size_x + 1, y * size_y + 1, size_x - 2, size_y - 2);

                    }
                    case CIRCLE -> {
                        setColor(g, backs[x][y]);
                        g.fillRect(x * size_x, y * size_y, size_x, size_y);
                        setColor(g, fronts[x][y]);
                        g.fillOval(x * size_x, y * size_y, size_x, size_y);
                    }
                    case ARROW_UP -> {
                        int[] xs = new int[]{x * size_x, x * size_x + (size_x / 2), (x + 1) * size_x - 1};
                        int[] ys = new int[]{(y + 1) * size_y - 1, y * size_y, (y + 1) * size_y - 1};
                        setColor(g, backs[x][y]);
                        g.fillRect(x * size_x, y * size_y, size_x, size_y);
                        setColor(g, fronts[x][y]);
                        g.fillPolygon(xs, ys, 3);
                    }
                    case ARROW_RIGHT -> {
                        int[] xs = new int[]{x * size_x, (x + 1) * size_x - 1, x * size_x};
                        int[] ys = new int[]{y * size_y, y * size_y + (size_y / 2), (y + 1) * size_y - 1};
                        setColor(g, backs[x][y]);
                        g.fillRect(x * size_x, y * size_y, size_x, size_y);
                        setColor(g, fronts[x][y]);
                        g.fillPolygon(xs, ys, 3);
                    }
                    case ARROW_DOWN -> {
                        int[] xs = new int[]{(x + 1) * size_x - 1, x * size_x + (size_x / 2), x * size_x};
                        int[] ys = new int[]{y * size_y, (y + 1) * size_y - 1, y * size_y};
                        setColor(g, backs[x][y]);
                        g.fillRect(x * size_x, y * size_y, size_x, size_y);
                        setColor(g, fronts[x][y]);
                        g.fillPolygon(xs, ys, 3);
                    }
                    case ARROW_LEFT -> {
                        int[] xs = new int[]{(x + 1) * size_x - 1, x * size_x, (x + 1) * size_x - 1};
                        int[] ys = new int[]{(y + 1) * size_y - 1, y * size_y + (size_y / 2), y * size_y};
                        setColor(g, backs[x][y]);
                        g.fillRect(x * size_x, y * size_y, size_x, size_y);
                        setColor(g, fronts[x][y]);
                        g.fillPolygon(xs, ys, 3);
                    }
                }
            }
        }
    }
}
