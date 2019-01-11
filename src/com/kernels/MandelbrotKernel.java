package com.kernels;

import com.util.Viewport;

public class MandelbrotKernel extends FractalKernel {

    public MandelbrotKernel(Viewport viewport, int maxIt, int numColours) {
        super(viewport, maxIt, numColours);
    }

    @Override
    public void run() {

        final int i = getGlobalId();

        final double c_r = (double)(i % screenWidth - translateX) / zoom + x1;
        final double c_i = (double)(i / screenWidth - translateY) / zoom + y1;

        /*double z_r = 0, z_i = 0, tmp;
        int it = 0;

        while ((it < maxIteration) && ((z_r * z_r) + (z_i * z_i) < 4)) {
            tmp = z_r;
            z_r = z_r*z_r - z_i*z_i + c_r;
            z_i = 2*z_i*tmp + c_i;
            it++;
        }*/

        int it = iterate(c_r, c_i, 0, 0);

        if (it == maxIteration)
            rgb[i] = 0;
        else {
//            double smooth = it + 1 - log2(0.5 * log(z_r * z_r + z_i * z_i));
//            rgb[i] = palette[(int) (smooth / maxIteration * numColours)];

            rgb[i] = palette[it % numColours];
        }
    }
}
