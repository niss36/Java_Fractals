package com.util;

import javax.swing.*;

public class ToolbarComponentsGroup implements ToolbarComponent {

    private final JComponent[] components;

    public ToolbarComponentsGroup(JComponent[] components) {

        this.components = components;
    }

    @Override
    public void addTo(JPanel panel) {
        for (JComponent component : components)
            panel.add(component);
    }
}
