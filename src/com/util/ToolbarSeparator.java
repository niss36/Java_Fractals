package com.util;

import javax.swing.*;
import java.awt.*;

public class ToolbarSeparator implements ToolbarComponent {

    private final Component separator;

    public ToolbarSeparator(Component separator) {
        this.separator = separator;
    }

    @Override
    public void addTo(JPanel panel) {
        panel.add(separator);
    }
}
