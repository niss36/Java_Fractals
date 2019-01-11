package com.gui.params;

import com.gui.panels.PanelFractal;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AngleProvider extends ParamProvider {

    private final double minAngle;
    private final double maxAngle;
    private final double defaultAngle;

    private double angle;

    private JTextField angleField;

    public AngleProvider(PanelFractal fractal, double minAngle, double maxAngle, double defaultAngle) {
        super(fractal);
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.defaultAngle = defaultAngle;

        angle = defaultAngle;
    }

    public double get() {
        return angle;
    }

    @Override
    public void reset() {
        if (angle != defaultAngle) {
            angle = defaultAngle;
            angleField.setText(String.valueOf(angle));

            fractal.markInvalid();
        }
    }

    private double parse(String s) {
        if (s.isEmpty())
            return defaultAngle;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return defaultAngle;
        }
    }

    @Override
    public JComponent[] createToolbarGroup() {

        JLabel angleLabel = new JLabel("Angle: ");

        angleField = new JTextField(String.valueOf(defaultAngle));
        angleField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if (!(Character.isDigit(key) || key == KeyEvent.VK_PERIOD || key == KeyEvent.VK_MINUS))
                    e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                double newAngle = parse(angleField.getText());

                if (newAngle < minAngle) {
                    newAngle = minAngle;
                    angleField.setText(String.valueOf(newAngle));
                }
                if (newAngle > maxAngle) {
                    newAngle = maxAngle;
                    angleField.setText(String.valueOf(newAngle));
                }

                if (angle != newAngle) {
                    angle = newAngle;
                    fractal.markInvalid();
                }
            }
        });

        return new JComponent[] {angleLabel, angleField};
    }
}
