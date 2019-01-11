package com.gui.params;

import com.gui.panels.PanelFractal;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DepthProvider extends ParamProvider {

    private final int defaultDepth;

    private int depth;

    private JTextField depthField;
    private JButton loop;

    private final int loopMax;
    private final Timer timer;
    private boolean goingUp;

    public DepthProvider(PanelFractal fractal, int defaultDepth, int loopMax) {
        super(fractal);
        this.defaultDepth = defaultDepth;
        this.loopMax = loopMax;

        depth = defaultDepth;
        timer = new Timer(500, e -> {
            if (goingUp) {
                if (depth < this.loopMax)
                    set(depth + 1, true, true);
                else {
                    set(this.loopMax - 1, true, true);
                    goingUp = false;
                }
            } else {
                if (depth > 0)
                    set(depth - 1, true, true);
                else {
                    set(1, true, true);
                    goingUp = true;
                }
            }
        });
    }

    public int get() {
        return depth;
    }

    private void set(int newDepth, boolean setText, boolean redraw) {
        if (newDepth < 0)
            newDepth = 0;

        if (depth != newDepth) {
            depth = newDepth;
            if (setText)
                depthField.setText(String.valueOf(newDepth));
            if (redraw)
                fractal.redraw();
            else
                fractal.markInvalid();
        }
    }

    @Override
    public void reset() {
        set(defaultDepth, true, false);
    }

    private int parse(String s) {
        if (s.isEmpty())
            return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultDepth;
        }
    }

    @Override
    public JComponent[] createToolbarGroup() {

        JLabel depthLabel = new JLabel("Depth: ");

        depthField = new JTextField(String.valueOf(depth));
        depthField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if (!(Character.isDigit(key)))
                    e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int newDepth = parse(depthField.getText());

                set(newDepth, false, false);
            }
        });

        JButton plus = new JButton("+");
        JButton minus = new JButton("-");
        loop = new JButton("Loop");

        plus.addActionListener(e -> set(depth + 1, true, true));
        minus.addActionListener(e -> set(depth - 1, true, true));
        loop.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                loop.setText("Loop");
            } else {
                set(0, true, true);
                goingUp = true;
                timer.restart();
                loop.setText("Stop");
            }
        });

        return new JComponent[] {depthLabel, depthField, plus, minus, loop};
    }
}
