package com.gui.groups;

import com.util.Viewport;

import javax.swing.*;

public class ZoomComponent extends ViewComponent {

    private final Viewport viewport;

    private final JLabel zoomLabel = new JLabel();

    public ZoomComponent(Viewport viewport) {
        this.viewport = viewport;
    }

    public void update() {
        String text = String.format("Zoom: 2^%d", viewport.getZoomPower());

        SwingUtilities.invokeLater(() -> zoomLabel.setText(text));
    }

    @Override
    public JComponent[] createToolbarGroup() {
        return new JComponent[] {zoomLabel};
    }
}
