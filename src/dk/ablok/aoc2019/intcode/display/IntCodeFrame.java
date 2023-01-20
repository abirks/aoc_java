package dk.ablok.aoc2019.intcode.display;

import javax.swing.*;

class IntCodeFrame extends JFrame {
    Blocks blocks;
    int offset_x, offset_y;

    public IntCodeFrame(String title, int res_x, int res_y, int size_x, int size_y, int offset_x, int offset_y, DisplayBlock.Shape shape, DisplayBlock.Color color) {
        setTitle(title);
        this.offset_x = offset_x;
        this.offset_y = offset_y;
        setSize(size_x * res_x, size_y * res_y + 37);
        blocks = new Blocks(res_x, res_y, size_x, size_y, shape, color);
        add(blocks);
        setVisible(true);
    }

    public InputMap getInputMap() {
        return blocks.getInputMap();
    }

    public ActionMap getActionMap() {
        return blocks.getActionMap();
    }

    public void draw(DisplayBlock block) {
        blocks.change(block.x + offset_x, block.y + offset_y, block.shape, block.front_color, block.back_color);
        repaint();
    }
}
