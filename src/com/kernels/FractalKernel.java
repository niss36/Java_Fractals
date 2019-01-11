package com.kernels;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;
import com.aparapi.internal.kernel.KernelManager;
import com.aparapi.internal.kernel.KernelManagers;
import com.util.Util;
import com.util.Vec3Colour;
import com.util.Viewport;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public abstract class FractalKernel extends Kernel {

    protected final double x1;
    protected final double y1;

    protected final int screenWidth;

    protected final int translateX;
    protected final int translateY;

    protected final long zoom;
    protected final int maxIteration;

    protected final int numColours;
    protected final int[] palette;

    private final BufferedImage image;
    protected final int[] rgb;

    public FractalKernel(Viewport viewport, int maxIt, int numColours) {

        x1 = viewport.getZ1().re;
        y1 = viewport.getZ1().im;

        screenWidth = viewport.getScreenWidth();

        translateX = viewport.getTranslateX();
        translateY = viewport.getTranslateY();

        zoom = viewport.getZoom();
        maxIteration = maxIt;

        this.numColours = numColours;
        palette = genPalette();

        image = new BufferedImage(viewport.getScreenWidth(), viewport.getScreenHeight(), BufferedImage.TYPE_INT_RGB);

        Device device = KernelManager.instance().bestDevice();
        int workGroupSize = device.getMaxWorkGroupSize();

        int imageSize = viewport.getScreenWidth() * viewport.getScreenHeight();
        int workingSize = imageSize % workGroupSize == 0 ? imageSize : workGroupSize * (imageSize / workGroupSize + 1);

        rgb = new int[workingSize];
    }

    protected int[] genPalette() {

        // [1,1,1],[1,0.8,0],[0.53,0.12,0.075],[0,0,0.6],[0,0.4,1],[1,1,1]

        Vec3Colour one = new Vec3Colour(1f, 1f, 1f);
        Vec3Colour two = new Vec3Colour(1f, 0.8f, 0f);
        Vec3Colour three = new Vec3Colour(0.53f, 0.12f, 0.075f);
        Vec3Colour four = new Vec3Colour(0f, 0f, 0.6f);
        Vec3Colour five = new Vec3Colour(0f, 0.4f, 1f);

        /*int[] p = new int[numColours];

        for (int i = 0; i < numColours; i++) {
            p[i] = Color.HSBtoRGB(0.60f + (5f * i) / numColours, 0.6f, 1.0f);
        }

        return p;*/
        return Util.generatePalette(numColours, one, two, three, four, five);
    }

    protected final int iterate(double c_r, double c_i, double z_r, double z_i) {

        /*double sq_z_r;
        double sq_z_i;
        double double_z_iz_r;

        int it = 0;

        for (; it < maxIteration; it++) {

            sq_z_r = z_r * z_r;
            sq_z_i = z_i * z_i;

            if (sq_z_r + sq_z_i >= 4)
                break;

            double_z_iz_r = 2 * z_i * z_r;

            z_r = sq_z_r - sq_z_i + c_r;
            z_i = double_z_iz_r + c_i;
        }*/

        double tmp;

        int it = 0;

        while ((it < maxIteration) && ((z_r * z_r) + (z_i * z_i) < 4)) {
            tmp = z_r;
            z_r = z_r*z_r - z_i*z_i + c_r;
            z_i = 2*z_i*tmp + c_i;
            it++;
        }

        return it;
    }

    public BufferedImage compute() {
        return compute(KernelManager.instance().bestDevice());
    }

    public BufferedImage compute(Device device) {

        Range r = device.createRange(rgb.length);

        execute(r);

        int[] dest = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(rgb, 0, dest, 0, dest.length);

        return image;
    }
}
