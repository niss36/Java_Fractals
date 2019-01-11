package com.gui.params;

import com.gui.panels.PanelFractal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ColourProvider extends ParamProvider {

    private final Color defaultColour;
    private final String defaultColourStr;

    private Color colour;

    private JTextField colourField;

    public ColourProvider(PanelFractal fractal, Color defaultColour, String defaultColourStr) {

        super(fractal);

        this.defaultColour = defaultColour;
        this.defaultColourStr = defaultColourStr;

        colour = defaultColour;
    }

    public Color get() {
        return colour;
    }

    @Override
    public void reset() {
        if (colour != defaultColour) {
            colour = defaultColour;
            colourField.setText(defaultColourStr);

            fractal.markInvalid();
        }
    }

    private Color parse(String s) {

        try {
            return (Color) Color.class.getField(s.toUpperCase()).get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            return defaultColour;
        }
    }

    @Override
    public JComponent[] createToolbarGroup() {

        JLabel colourLabel = new JLabel("Colour: ");

        colourField = new JTextField(defaultColourStr);
        colourField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if (Character.isDigit(key))
                    e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Color newColour = parse(colourField.getText());

                if (!colour.equals(newColour)) {
                    colour = newColour;
                    fractal.markInvalid();
                }
            }
        });

        return new JComponent[] {colourLabel, colourField};
    }
}
