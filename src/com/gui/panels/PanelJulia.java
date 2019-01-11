package com.gui.panels;

import com.gui.Frame;
import com.gui.params.ComplexProvider;
import com.kernels.FractalKernel;
import com.kernels.JuliaKernel;
import com.util.Complex;
import com.util.ToolbarBuilder;
import com.util.Viewport;

public class PanelJulia extends PanelFractalComplex {

    private final ComplexProvider c = new ComplexProvider(this, "c");

    public PanelJulia(Frame frame) {
        super(frame, "Julia");
    }

    @Override
    public Complex defaultZ1() {
        return new Complex(-1, -1.2);
    }

    @Override
    public Complex defaultZ2() {
        return new Complex(1, 1.2);
    }

    @Override
    protected FractalKernel createKernel(Viewport viewport, int numColours) {
        return new JuliaKernel(viewport, getValue(), numColours, c.get());
    }

    @Override
    protected void buildToolbar(ToolbarBuilder builder) {
        super.buildToolbar(builder);

        builder.insertGroup(3, c.createToolbarGroup())
                .insertGap(3, 20)
                .insertGap(4, 50);
    }
}
