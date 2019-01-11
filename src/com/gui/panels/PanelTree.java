package com.gui.panels;

import com.gui.Frame;
import com.gui.params.AngleProvider;
import com.gui.params.ColourProvider;
import com.gui.params.DepthProvider;
import com.gui.params.TypeProvider;
import com.util.ToolbarBuilder;
import com.util.Turtle;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelTree extends PanelFractal {

    private static final int TREE = 0, PYTHAGORAS = 1;
    private final TypeProvider type = new TypeProvider(this, "Tree", "Pythagoras' Tree");
    private final DepthProvider depth = new DepthProvider(this, 1, 15);
    private final ColourProvider colour = new ColourProvider(this, Color.black, "black");
    private final AngleProvider angle = new AngleProvider(this, -360, 360, 0);

    public PanelTree(Frame frame) {
        super(frame, "Tree", "Length: ");
    }

    @Override
    protected void draw() {

        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(colour.get());

        int length = getValue();
        int type = this.type.get();

        int startX = type == TREE ? getWidth() / 2 : type == PYTHAGORAS ? getWidth() / 2 + length / 2 : 0;
        int startY = getHeight() - 5;

        Turtle t = new Turtle(g, startX, startY);

        t.left(180);

        switch (type) {
            case TREE:
                drawTree(t, length, depth.get(), angle.get());
                break;

            case PYTHAGORAS:
                drawPythagoras(t, length, depth.get(), angle.get());
                break;
        }

        this.image = image;
    }

    private void drawTree(Turtle t, double length, int depth, double angle) {

        if (length > 1 && depth > 0) {

            double l = length / 3;

            double a = 45;

            int branches = 2;

            branches--;

            depth--;

            t.forward(length);

            t.right(a + angle);

            for (int i = 0; i < branches; i++) {
                drawTree(t, length - l, depth, angle);

                t.left(2 * a / (branches));
            }

            drawTree(t, length - l, depth, angle);

            t.right(a - angle);

            t.back(length);

        } else {

            t.forward(length);
            t.back(length);
        }
    }

    private void drawPythagoras(Turtle t, double length, int depth, double angle) {

        if (length > 1 && depth > 0) {

            double a1 = 45 + angle;
            double a2 = 45 - angle;

            double l1 = Math.cos(Math.toRadians(a1)) * length;
            double l2 = Math.cos(Math.toRadians(a2)) * length;

            depth--;

            t.drawPolygon(length, 4);

            t.forward(length);

            t.right(a1);

            drawPythagoras(t, l1, depth, angle);

            t.left(90);

            t.forward(l1);

            drawPythagoras(t, l2, depth, angle);

            t.left(90);

            t.forward(l2);

            t.left(a1);
            t.forward(length);
            t.left(90);
            t.forward(length);
            t.left(90);

        } else t.drawPolygon(length, 4);
    }

    @Override
    public int defaultValue() {
        int[] values = {300, 200};
        return values[type.get()];
    }

    @Override
    public void reset() {

        depth.reset();
        colour.reset();
        angle.reset();

        super.reset();
    }

    @Override
    protected void onResized() {

    }

    @Override
    public String getAdditionalInfo() {
        return String.format("%d-%d-%.1f", getValue(), depth.get(), angle.get());
    }

    @Override
    protected void buildToolbar(ToolbarBuilder builder) {
        builder.addGroup(type.createToolbarGroup())
                .addGap(20)
                .addGroup(depth.createToolbarGroup())
                .addGap(50)
                .addGroup(colour.createToolbarGroup())
                .addGap(50)
                .addGroup(angle.createToolbarGroup());
    }
}
