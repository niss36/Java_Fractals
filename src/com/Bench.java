package com;

import com.aparapi.device.Device;
import com.aparapi.internal.kernel.KernelManager;
import com.aparapi.internal.kernel.KernelManagers;
import com.kernels.FractalKernel;
import com.kernels.MandelbrotKernel;
import com.util.Complex;
import com.util.Viewport;

//TODO wip
public class Bench {

    private static class BenchViewport extends Viewport {

        private int translateX;
        private int translateY;

        public BenchViewport() {
            super(null);
        }

        private void computeTranslate() {
            Complex dimensions = getZ2().sub(getZ1()).multiply(getZoom());

            translateX = (int) Math.round(getScreenWidth() / 2d - dimensions.re / 2d);
            translateY = (int) Math.round(getScreenHeight() / 2d - dimensions.im / 2d);
        }

        @Override
        public Complex getZ1() {
            return super.getZ1();
        }

        @Override
        public Complex getZ2() {
            return super.getZ2();
        }

        @Override
        public Complex getCenter() {
            return new Complex(-1.7862752526998504, 1.1978298425683322E-4);
        }

        @Override
        public long getZoom() {
            return 1 << 28;
        }

        @Override
        public int getZoomPower() {
            return 28;
        }

        @Override
        public int getScreenWidth() {
            return 1920;
        }

        @Override
        public int getScreenHeight() {
            return 965;
        }

        @Override
        public int getTranslateX() {
            return translateX;
        }

        @Override
        public int getTranslateY() {
            return translateY;
        }
    }

    private static final int MAX_IT = 5_000;
    private static final int NUM_COL = 250;

    public static void main(String[] args) {

        Device best = KernelManager.instance().bestDevice();
        Device jtp = KernelManagers.JTP_ONLY.bestDevice();

        Runnable computeBest = () -> {
            FractalKernel kernel = new MandelbrotKernel(new BenchViewport(), MAX_IT, NUM_COL);
            kernel.compute(best);
            kernel.dispose();
        };

        Runnable computeJTP = () -> {
            FractalKernel kernel = new MandelbrotKernel(new BenchViewport(), MAX_IT, NUM_COL);
            kernel.compute(jtp);
            kernel.dispose();
        };

        System.out.println("BEST");
        time(computeBest);

        System.out.println();
        System.out.println("JTP");
        time(computeJTP);
    }

    private static final int WARM_UP = 10;
    private static final int TIMED = 1_000;

    private static void time(Runnable task) {

        for (int i = 0; i < WARM_UP; i++)
            task.run();

        System.out.println("START __________");
        System.out.print  ("      ");

        double mean = 0;

        for (int i = 0; i < TIMED; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();

            long elapsed = end - start;

            mean += (elapsed - mean) / (i + 1);

            if ((i + 1) % (TIMED / 10) == 0)
                System.out.print('.');
        }

        System.out.println();
        System.out.println("END - " + mean / 1_000_000 + "ms");
    }
}
