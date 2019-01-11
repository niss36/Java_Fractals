package com.gui.panels;

import com.gui.Frame;
import com.util.ToolbarBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//TODO: separate the actual fractal from the panel
public abstract class PanelFractal extends JPanel {

    private final Frame frame;
    private final String name;
    private final String valueLabelText;

    private int value;

    private final BlockingQueue<Object> drawQueue = new LinkedBlockingQueue<>(); //TODO meaningful events
    private boolean invalid = true;

    protected BufferedImage image;
    private JPanel toolBar;

    protected PanelFractal(Frame frame, String name, String valueLabelText) {
        this.frame = frame;
        this.name = name;
        this.valueLabelText = valueLabelText;

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                markInvalid();
                if (isVisible())
                    onResized();
            }
        });

        Thread drawThread = new Thread(() -> {
            boolean doDraw = true;
            while (doDraw) {
                try {
                    Object o = drawQueue.take(); //TODO
                    draw();
                    repaint();
                } catch (InterruptedException e) {
                    doDraw = false;
                }
            }
        }, "Thread-" + name);

        drawThread.setDaemon(true);
        drawThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, null);
    }

    protected abstract void draw();

    public void redraw() {
        drawQueue.add(new Object()); //TODO
        invalid = false;
    }

    public void markInvalid() {
        invalid = true;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        frame.setValue(newValue);
        actualise(newValue);
    }

    public void actualise(int newValue) {

        if (value != newValue || invalid) {
            value = newValue;
            redraw();
        }
    }

    public abstract int defaultValue();

    public void reset() {
        setValue(defaultValue());
    }

    protected void onResized() {
        setValue(defaultValue());
    }

    public String getName() {
        return name;
    }

    public String getValueLabelText() {
        return valueLabelText;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getAdditionalInfo() {
        return String.format("%d", value);
    }

    protected abstract void buildToolbar(ToolbarBuilder builder);

    public JPanel getToolbar() {
        if (toolBar == null) {
            ToolbarBuilder builder = new ToolbarBuilder();
            buildToolbar(builder);
            toolBar = builder.build();
        }

        return toolBar;
    }
}
