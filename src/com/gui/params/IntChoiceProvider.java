package com.gui.params;

import com.gui.panels.PanelFractal;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

public class IntChoiceProvider extends ParamProvider {

    private final String labelStr;

    private final int defaultIndex;
    private final int[] choices;

    private int choice;
    private JComboBox<Integer> comboBox;
    private ItemListener listener;

    public IntChoiceProvider(PanelFractal fractal, String labelStr, int defaultIndex, int... choices) {
        super(fractal);
        this.labelStr = labelStr;
        this.defaultIndex = defaultIndex;
        this.choices = choices;

        choice = choices[defaultIndex];
    }

    public int get() {
        return choice;
    }

    @Override
    public void reset() {

        int newChoice = choices[defaultIndex];

        if (choice != newChoice) {
            choice = newChoice;

            // Prevent unwanted call to redraw()
            comboBox.removeItemListener(listener);
            comboBox.setSelectedIndex(defaultIndex);
            comboBox.addItemListener(listener);

            fractal.markInvalid();
        }
    }

    @Override
    public JComponent[] createToolbarGroup() {

        JLabel choiceLabel = new JLabel(labelStr + ": ");

        Integer[] boxed = Arrays.stream(choices).boxed().toArray(Integer[]::new);
        comboBox = new JComboBox<>(boxed);
        comboBox.setSelectedIndex(defaultIndex);
        listener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                choice = choices[comboBox.getSelectedIndex()];

                fractal.markInvalid();
                fractal.redraw();
            }
        };
        comboBox.addItemListener(listener);

        comboBox.setMaximumSize(comboBox.getMinimumSize());

        return new JComponent[] {choiceLabel, comboBox};
    }
}
