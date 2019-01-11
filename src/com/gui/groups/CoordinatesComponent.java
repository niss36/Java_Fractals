package com.gui.groups;

import com.util.Complex;
import com.util.Viewport;

import javax.swing.*;

public class CoordinatesComponent extends ViewComponent {

    private final Viewport viewport;

    private final JLabel coordinatesLabel = new JLabel();

    public CoordinatesComponent(Viewport viewport) {
        this.viewport = viewport;
    }

    public void update() {
        Complex c = viewport.getCenter();
        String text = String.format("x: %s, y: %s", c.re, c.im);

        SwingUtilities.invokeLater(() -> coordinatesLabel.setText(text));
    }

    @Override
    public JComponent[] createToolbarGroup() {
        return new JComponent[] {coordinatesLabel};
    }
}
