package com.gui.panels;

import com.gui.Frame;
import com.gui.params.ColourProvider;
import com.util.ToolbarBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelSierpinskiCarpet extends PanelFractal {

    private final ColourProvider colour = new ColourProvider(this, Color.black, "black");

    public PanelSierpinskiCarpet(Frame frame) {
        super(frame, "Sierpinski Carpet", "Length: ");
    }

    @Override
    protected void draw() {

        int length = getValue();

        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        int translateX = getWidth() / 2 - length / 2;
        int translateY = getHeight() / 2 - length / 2;

        for (int x = 0; x < length; x++) {

            for (int y = 0; y < length; y++) {

                if (isPixelFilled(x, y)) {
                    int x1 = x + translateX - 1;
                    int y1 = y + translateY - 1;

                    if (!(x1 >= getWidth() || x1 < 0 || y1 >= getHeight() || y1 < 0)) image.setRGB(x1, y1, colour.get().getRGB());
                }
            }
        }

        this.image = image;
    }

    private boolean isPixelFilled(int x, int y) {
        while (x > 0 || y > 0) {
            if (x % 3 == 1 && y % 3 == 1)
                return false;
            x /= 3;
            y /= 3;
        }
        return true;
    }

    @Override
    public int defaultValue() {

        int pow3 = 1;

        while (pow3 * 3 <= Math.min(getWidth(), getHeight()))
            pow3 *= 3;

        return pow3;
    }

    @Override
    public void reset() {

        colour.reset();

        super.reset();
    }

    @Override
    protected void buildToolbar(ToolbarBuilder builder) {
        builder.addGap(5)
                .addGroup(colour.createToolbarGroup());
    }
}
