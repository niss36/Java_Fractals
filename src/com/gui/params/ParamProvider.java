package com.gui.params;

import com.gui.panels.PanelFractal;

import javax.swing.*;

public abstract class ParamProvider {

    protected final PanelFractal fractal;

    public ParamProvider(PanelFractal fractal) {
        this.fractal = fractal;
    }

    public abstract void reset();

    public abstract JComponent[] createToolbarGroup();
}
