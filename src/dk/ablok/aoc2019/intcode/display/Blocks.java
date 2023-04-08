package dk.ablok.aoc2019.intcode.display;

import javax.swing.*;
import java.awt.*;

class Blocks extends JComponent {
    int resolutionX;
    int resolutionY;
    int sizeX;
    int sizeY;
    DisplayBlock.Shape[][] shapes;
    DisplayBlock.Color[][] frontColors, backColors;
    DisplayBlock.Color defaultColor;

    @Deprecated
    public Blocks(int resolutionX, int resolutionY, int sizeX, int sizeY, DisplayBlock.Shape defaultShape, DisplayBlock.Color defaultColor) {
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.defaultColor = defaultColor;

        frontColors = new DisplayBlock.Color[resolutionX][resolutionY];
        backColors = new DisplayBlock.Color[resolutionX][resolutionY];
        shapes = new DisplayBlock.Shape[resolutionX][resolutionY];

        for (int y = 0; y < resolutionY; y++) {
            for (int x = 0; x < resolutionX; x++) {
                shapes[x][y] = defaultShape;
                frontColors[x][y] = defaultColor;
                backColors[x][y] = defaultColor;
            }
        }
    }

    public void change(int x, int y, DisplayBlock.Shape shape, DisplayBlock.Color frontColor, DisplayBlock.Color backColor) {
        if (0 <= x && x < resolutionX && 0 <= y && y < resolutionY) {
            shapes[x][y] = shape;
            frontColors[x][y] = frontColor;
            backColors[x][y] = backColor;
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

        for (int y = 0; y < resolutionY; y++) {
            for (int x = 0; x < resolutionX; x++) {
                // Set shape and color
                switch (shapes[x][y]) {
                    case RECTANGLE -> {
                        setColor(g, frontColors[x][y]);
                        g.fillRect(x * sizeX, y * sizeY, sizeX, sizeY);
                    }
                    case EMPTY_RECTANGLE -> {
                        setColor(g, frontColors[x][y]);
                        g.fillRect(x * sizeX, y * sizeY, sizeX, sizeY);
                        setColor(g, backColors[x][y]);
                        g.fillRect(x * sizeX + 1, y * sizeY + 1, sizeX - 2, sizeY - 2);

                    }
                    case CIRCLE -> {
                        setColor(g, backColors[x][y]);
                        g.fillRect(x * sizeX, y * sizeY, sizeX, sizeY);
                        setColor(g, frontColors[x][y]);
                        g.fillOval(x * sizeX, y * sizeY, sizeX, sizeY);
                    }
                    case ARROW_UP -> {
                        int[] xs = new int[]{x * sizeX, x * sizeX + (sizeX / 2), (x + 1) * sizeX - 1};
                        int[] ys = new int[]{(y + 1) * sizeY - 1, y * sizeY, (y + 1) * sizeY - 1};
                        setColor(g, backColors[x][y]);
                        g.fillRect(x * sizeX, y * sizeY, sizeX, sizeY);
                        setColor(g, frontColors[x][y]);
                        g.fillPolygon(xs, ys, 3);
                    }
                    case ARROW_RIGHT -> {
                        int[] xs = new int[]{x * sizeX, (x + 1) * sizeX - 1, x * sizeX};
                        int[] ys = new int[]{y * sizeY, y * sizeY + (sizeY / 2), (y + 1) * sizeY - 1};
                        setColor(g, backColors[x][y]);
                        g.fillRect(x * sizeX, y * sizeY, sizeX, sizeY);
                        setColor(g, frontColors[x][y]);
                        g.fillPolygon(xs, ys, 3);
                    }
                    case ARROW_DOWN -> {
                        int[] xs = new int[]{(x + 1) * sizeX - 1, x * sizeX + (sizeX / 2), x * sizeX};
                        int[] ys = new int[]{y * sizeY, (y + 1) * sizeY - 1, y * sizeY};
                        setColor(g, backColors[x][y]);
                        g.fillRect(x * sizeX, y * sizeY, sizeX, sizeY);
                        setColor(g, frontColors[x][y]);
                        g.fillPolygon(xs, ys, 3);
                    }
                    case ARROW_LEFT -> {
                        int[] xs = new int[]{(x + 1) * sizeX - 1, x * sizeX, (x + 1) * sizeX - 1};
                        int[] ys = new int[]{(y + 1) * sizeY - 1, y * sizeY + (sizeY / 2), y * sizeY};
                        setColor(g, backColors[x][y]);
                        g.fillRect(x * sizeX, y * sizeY, sizeX, sizeY);
                        setColor(g, frontColors[x][y]);
                        g.fillPolygon(xs, ys, 3);
                    }
                }
            }
        }
    }
}
