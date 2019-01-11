package com.gui.panels;

import com.gui.Frame;
import com.gui.groups.CoordinatesComponent;
import com.gui.groups.DrawTimeComponent;
import com.gui.groups.ZoomComponent;
import com.gui.params.IntChoiceProvider;
import com.kernels.FractalKernel;
import com.util.Complex;
import com.util.ToolbarBuilder;
import com.util.Viewport;
import com.util.ZoomLimitException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class PanelFractalComplex extends PanelFractal {

    private final Viewport viewport = new Viewport(this);

    private final IntChoiceProvider numColours = new IntChoiceProvider(this, "Colours", 2, 50, 100, 250, 500, 1000);
    private final ZoomComponent zoom = new ZoomComponent(viewport);
    private final CoordinatesComponent coords = new CoordinatesComponent(viewport);
    private final DrawTimeComponent timer = new DrawTimeComponent();

    protected PanelFractalComplex(Frame frame, String name) {
        super(frame, name, "Max iterations: ");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    zoomIn(e);
                else if (SwingUtilities.isRightMouseButton(e))
                    zoomOut(e);
            }
        });
    }

    @Override
    protected void draw() {

        timer.start();

        FractalKernel kernel = createKernel(viewport, numColours.get());

        try {
            image = kernel.compute();

            zoom.update();
            coords.update();
        } finally {
            kernel.dispose();
            timer.stop();
        }
    }

    @Override
    public int defaultValue() {
        return 100;
    }

    @Override
    public void reset() {
        viewport.reset();
        numColours.reset();
        markInvalid();

        super.reset();
    }

    @Override
    protected void onResized() {
        viewport.onResized();
        redraw();
    }

    public abstract Complex defaultZ1();

    public abstract Complex defaultZ2();

    public Complex defaultCenter() {
        return Complex.ZERO;
    }

    public int defaultZoomPower() {
        return 8;
    }

    protected abstract FractalKernel createKernel(Viewport viewport, int numColours);

    @Override
    protected void buildToolbar(ToolbarBuilder builder) {
        builder.addGap(5)
                .addGroup(numColours.createToolbarGroup())
                .addGap(20)
                .addGroup(zoom.createToolbarGroup())
                .addGap(20)
                .addGroup(coords.createToolbarGroup())
                .addGlue()
                .addGroup(timer.createToolbarGroup())
                .addGap(5);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem itemCopyClipboard = new JMenuItem("Copy coordinates to clipboard");

        itemCopyClipboard.addActionListener(
                e -> Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                        new StringSelection(viewport.getCenter().toString()), null));

        popupMenu.add(itemCopyClipboard);

        JPanel panel = builder.getPanel();
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    popupMenu.show(panel, e.getX(), e.getY());
            }
        });
    }

    private void zoomIn(MouseEvent e) {

        try {
            viewport.zoomIn(e.getX(), e.getY());

            // Update max iteration
            setValue(getValue() + defaultValue() / 4);
        } catch (ZoomLimitException e1) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void zoomOut(MouseEvent e) {

        try {
            viewport.zoomOut(e.getX(), e.getY());

            // Update max iteration
            setValue(getValue() - defaultValue() / 4);
        } catch (ZoomLimitException e1) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
