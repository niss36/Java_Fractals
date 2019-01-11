package com.util;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ToolbarBuilder {

    private final JPanel panel = new JPanel();
    private final List<ToolbarComponent> components = new LinkedList<>();

    public ToolbarBuilder addGroup(JComponent... groupComponents) {

        components.add(new ToolbarComponentsGroup(groupComponents));
        return this;
    }

    public ToolbarBuilder addGap(int width) {

        components.add(new ToolbarSeparator(Box.createRigidArea(new Dimension(width, 0))));
        return this;
    }

    public ToolbarBuilder addGlue() {

        components.add(new ToolbarSeparator(Box.createHorizontalGlue()));
        return this;
    }

    public ToolbarBuilder insertGroup(int groupIndex, JComponent... groupComponents) {

        components.add(getListIndex(groupIndex), new ToolbarComponentsGroup(groupComponents));
        return this;
    }

    public ToolbarBuilder insertGap(int groupIndex, int width) {

        components.add(getListIndex(groupIndex), new ToolbarSeparator(Box.createRigidArea(new Dimension(width, 0))));
        return this;
    }

    private int getListIndex(int groupIndex) {

        int tmpIndex = groupIndex;

        int i = 0;
        for (ToolbarComponent component : components) {
            if (tmpIndex == 0) return i;
            if (component instanceof ToolbarComponentsGroup)
                tmpIndex--;
            i++;
        }

        throw new ArrayIndexOutOfBoundsException(groupIndex);
    }

    public JPanel build() {

        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        for (ToolbarComponent component : components)
            component.addTo(panel);

        return panel;
    }

    // Should be used only to add listeners or similar.
    public JPanel getPanel() {
        return panel;
    }
}
