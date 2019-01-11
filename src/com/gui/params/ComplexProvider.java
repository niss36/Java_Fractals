package com.gui.params;

import com.gui.panels.PanelFractal;
import com.util.Complex;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ComplexProvider extends ParamProvider {

    private final String labelStr;

    private Complex c = Complex.ZERO;

    private JTextField cField;

    public ComplexProvider(PanelFractal fractal, String labelStr) {
        super(fractal);

        this.labelStr = labelStr;
    }

    public Complex get() {
        return c;
    }

    @Override
    public void reset() {
        if (!c.equals(Complex.ZERO)) {
            c = Complex.ZERO;
            cField.setText(c.toString());

            fractal.markInvalid();
        }
    }

    private Complex parse(String s) {

        try {
            return Complex.parseComplex(s);
        } catch (NumberFormatException e) {
            return Complex.ZERO;
        }
    }

    @Override
    public JComponent[] createToolbarGroup() {

        JLabel cLabel = new JLabel(labelStr + ": ");

        cField = new JTextField(c.toString());
        cField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if (!(Character.isDigit(key) || key == KeyEvent.VK_PERIOD || key == KeyEvent.VK_MINUS || key == KeyEvent.VK_SEMICOLON || key == KeyEvent.VK_SPACE))
                    e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Complex newC = parse(cField.getText());

                if (!c.equals(newC)) {
                    c = newC;
                    fractal.markInvalid();
                }
            }
        });

        return new JComponent[] {cLabel, cField};
    }
}
