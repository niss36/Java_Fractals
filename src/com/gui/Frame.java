package com.gui;

import com.gui.panels.*;
import com.util.ToolbarBuilder;
import com.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Frame extends JFrame {

    private int value;
    private final JLabel valueLabel;
    private final JTextField valueField;

    private PanelFractal selected;

    private final CardLayout fractalsCL = new CardLayout();
    private final JPanel fractal = new JPanel();

    private final CardLayout toolbarsCL = new CardLayout();
    private final JPanel toolbar = new JPanel();

    private final PanelVonKoch vonKoch = new PanelVonKoch(this);
    private final PanelSierpinskiCarpet sierpinskiCarpet = new PanelSierpinskiCarpet(this);
    private final PanelTree tree = new PanelTree(this);
    private final PanelMandelbrot mandelbrot = new PanelMandelbrot(this);
    private final PanelJulia julia = new PanelJulia(this);
    private final PanelFractal[] choices = {vonKoch, sierpinskiCarpet, tree, mandelbrot, julia};
    private final String[] choicesNames = new String[choices.length];

    public Frame() {

        this.setSize(1000, 1000);
        this.setResizable(true);
        this.setTitle("Java Fractals");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel content = new JPanel();

        JPanel[] toolbars = new JPanel[choices.length];

        for (int i = 0; i < choices.length; i++) {
            toolbars[i] = choices[i].getToolbar();
            choicesNames[i] = choices[i].getName();
        }

        JComboBox comboBoxSelected = new JComboBox<>(choicesNames);
        comboBoxSelected.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
                showFractal(comboBoxSelected.getSelectedIndex());
        });

        valueField = new JTextField(String.valueOf(value));
        valueField.setMinimumSize(new Dimension(100, 30));

        valueField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if (!Character.isDigit(key))
                    e.consume();
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                value = valueField.getText().isEmpty() ? 0 : Integer.parseInt(valueField.getText());
            }
        });

        valueLabel = new JLabel("");

        JButton apply = new JButton("Refresh");
        JButton reset = new JButton("Reset");
        JButton valuePlus = new JButton("+");
        JButton valueMinus = new JButton("-");
        JButton screenshot = new JButton("Screen Shot");

        apply.addActionListener(e -> selected.actualise(value));

        reset.addActionListener(e -> selected.reset());

        valuePlus.addActionListener(e -> {
            setValue(value + 1);

            selected.actualise(value);
        });

        valueMinus.addActionListener(e -> {
            setValue(value - 1);

            selected.actualise(value);
        });

        screenshot.addActionListener(e -> {
            try {
                Util.takeScreenshot(selected);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                        apply.doClick();
                    return false;
                });

        ToolbarBuilder builder = new ToolbarBuilder();

        builder.addGroup(comboBoxSelected, reset, apply)
                .addGap(20)
                .addGroup(valueLabel, valueField, valuePlus, valueMinus)
                .addGap(20)
                .addGroup(screenshot);

        JPanel topMenu = builder.build();

        fractal.setLayout(fractalsCL);
        for (int i = 0; i < choices.length; i++) fractal.add(choices[i], choicesNames[i]);

        toolbar.setLayout(toolbarsCL);
        for (int i = 0; i < toolbars.length; i++) toolbar.add(toolbars[i], choicesNames[i]);

        content.setLayout(new BorderLayout());
        content.add(topMenu, BorderLayout.NORTH);
        content.add(fractal, BorderLayout.CENTER);
        content.add(toolbar, BorderLayout.SOUTH);

        setContentPane(content);
    }

    private void showFractal(int index) {
        fractalsCL.show(fractal, choicesNames[index]);
        toolbarsCL.show(toolbar, choicesNames[index]);
        selected = choices[index];

        selected.reset();

        valueLabel.setText(selected.getValueLabelText());
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < 0) return;
        this.value = value;
        valueField.setText(String.valueOf(value));
    }

    public void showFrame() {
        setVisible(true);
        showFractal(0);
    }
}
