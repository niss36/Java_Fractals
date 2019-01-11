package com.gui.panels;

import com.gui.Frame;
import com.gui.params.AngleProvider;
import com.gui.params.ColourProvider;
import com.gui.params.DepthProvider;
import com.util.ToolbarBuilder;
import com.util.Turtle;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelVonKoch extends PanelFractal {

    private final DepthProvider depth = new DepthProvider(this, 1, 8);
    private final ColourProvider colour = new ColourProvider(this, Color.black, "black");
    private final AngleProvider angle = new AngleProvider(this, 0, 90, 60);

    public PanelVonKoch(Frame frame) {
        super(frame, "Von-Koch", "Length: ");
    }

    @Override
    protected void draw() {

        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(colour.get());

        int length = getValue();

        int startX = getWidth() / 2 + length / 2 + 1;
        int startY = getHeight() - 5;

        Turtle t = new Turtle(g, startX, startY);
        t.right(90);

        drawVonKoch(t, length, depth.get(), angle.get());

        this.image = image;
    }

    private void drawVonKoch(Turtle t, double length, int depth, double angle) {

        if (depth > 0) {

            length /= 2 * (1 + Math.cos(Math.toRadians(angle)));
            depth--;

            drawVonKoch(t, length, depth, angle);

            t.right(angle);

            drawVonKoch(t, length, depth, angle);

            t.left(2 * angle);

            drawVonKoch(t, length, depth, angle);

            t.right(angle);

            drawVonKoch(t, length, depth, angle);

        } else t.forward(length);
    }

    @Override
    public int defaultValue() {
        return getWidth();
    }

    @Override
    public void reset() {

        depth.reset();
        colour.reset();
        angle.reset();

        super.reset();
    }

    @Override
    public String getAdditionalInfo() {
        return String.format("%d-%d-%.1f", getValue(), depth.get(), angle.get());
    }

    @Override
    protected void buildToolbar(ToolbarBuilder builder) {
        builder.addGap(5)
                .addGroup(depth.createToolbarGroup())
                .addGap(50)
                .addGroup(colour.createToolbarGroup())
                .addGap(50)
                .addGroup(angle.createToolbarGroup());
    }
}
