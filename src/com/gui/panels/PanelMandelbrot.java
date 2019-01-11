package com.gui.panels;

import com.gui.Frame;
import com.kernels.FractalKernel;
import com.kernels.MandelbrotKernel;
import com.util.Complex;
import com.util.Viewport;

public class PanelMandelbrot extends PanelFractalComplex {

    public PanelMandelbrot(Frame frame) {
        super(frame, "Mandelbrot");
    }

    @Override
    public Complex defaultZ1() {
        return new Complex(-2.1, -1.2);
    }

    @Override
    public Complex defaultZ2() {
        return new Complex(0.6, 1.2);
    }

    @Override
    protected FractalKernel createKernel(Viewport viewport, int numColours) {
        return new MandelbrotKernel(viewport, getValue(), numColours);
    }
}
