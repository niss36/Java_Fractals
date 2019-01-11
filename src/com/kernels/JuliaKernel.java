package com.kernels;

import com.util.Complex;
import com.util.Viewport;

public class JuliaKernel extends FractalKernel {

    private final double c_r;
    private final double c_i;

    public JuliaKernel(Viewport viewport, int maxIt, int numColours, Complex c) {
        super(viewport, maxIt, numColours);

        c_r = c.re;
        c_i = c.im;
    }

    @Override
    public void run() {

        final int i = getGlobalId();

        double z_r = (double)(i % screenWidth - translateX) / zoom + x1;
        double z_i = (double)(i / screenWidth - translateY) / zoom + y1;

        /*double tmp;

        int it = 0;

        while ((it < maxIteration) && ((z_r * z_r) + (z_i * z_i) < 4)) {
            tmp = z_r;
            z_r = z_r*z_r - z_i*z_i + c_r;
            z_i = 2*z_i*tmp + c_i;
            it++;
        }*/

        int it = iterate(c_r, c_i, z_r, z_i);

        if (it == maxIteration)
            rgb[i] = 0;
        else
            rgb[i] = palette[it % numColours];
    }
}
