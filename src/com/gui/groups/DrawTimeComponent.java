package com.gui.groups;

import javax.swing.*;

public class DrawTimeComponent extends ViewComponent {

    private boolean started = false;
    private long startTime;

    private JLabel drawTimeLabel;

    public void start() {
        if (started)
            throw new IllegalStateException("Timer was already started");

        started = true;
        startTime = System.nanoTime();

        SwingUtilities.invokeLater(() -> drawTimeLabel.setText("[Drawing]"));
    }

    public void stop() {
        if (started) {
            long elapsed = System.nanoTime() - startTime;

            double seconds = elapsed / 1_000_000_000d;
            String message = String.format("[Done drawing after %.4f seconds]", seconds);

            if (seconds >= 1) System.out.println(message);

            SwingUtilities.invokeLater(() -> drawTimeLabel.setText(message));

            started = false;

        } else throw new IllegalStateException("Timer was not started");
    }

    @Override
    public JComponent[] createToolbarGroup() {
        drawTimeLabel = new JLabel();

        return new JComponent[] {drawTimeLabel};
    }
}
