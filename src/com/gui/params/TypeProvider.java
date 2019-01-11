package com.gui.params;

import com.gui.panels.PanelFractal;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class TypeProvider extends ParamProvider {

    private final String[] choices;

    private int type;

    private JComboBox<String> comboBoxType;

    public TypeProvider(PanelFractal fractal, String... choices) {
        super(fractal);
        this.choices = choices;
    }

    public int get() {
        return type;
    }

    @Override
    public void reset() {
        // No-op
    }

    @Override
    public JComponent[] createToolbarGroup() {

        comboBoxType = new JComboBox<>(choices);
        comboBoxType.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                type = comboBoxType.getSelectedIndex();

                fractal.markInvalid();
                fractal.setValue(fractal.defaultValue());
            }
        });

        return new JComponent[] {comboBoxType};
    }
}
