package com.util;

import com.gui.panels.PanelFractalComplex;

public class Viewport {

    private final PanelFractalComplex fractal;

    private Complex z1, z2, center;
    private int zoomPower;
    private long zoom;

    private int screenWidth, screenHeight;
    private int translateX, translateY;

    public Viewport(PanelFractalComplex fractal) {
        this.fractal = fractal;
    }

    public void reset() {
        z1 = fractal.defaultZ1();
        z2 = fractal.defaultZ2();
        center = fractal.defaultCenter();
        zoomPower = fractal.defaultZoomPower();
        zoom = 1L << zoomPower; // 2^zoomPower

        onResized();
    }

    public void onResized() {
        screenWidth = fractal.getWidth();
        screenHeight = fractal.getHeight();

        computeTranslate();
    }

    private void computeTranslate() {
        Complex dimensions = z2.sub(z1).multiply(zoom);

        translateX = (int) Math.round(screenWidth / 2d - dimensions.re / 2d);
        translateY = (int) Math.round(screenHeight / 2d - dimensions.im / 2d);
    }

    public void zoomIn(int x, int y) throws ZoomLimitException {

        if (zoom * 2 <= 0) throw new ZoomLimitException("Can't zoom further in (" + zoom + ")");

        center = new Complex(x - translateX, y - translateY).divide(zoom).add(z1);

        Complex c = z2.sub(z1).divide(4);

        z1 = center.sub(c);
        z2 = center.add(c);

        zoomPower++;
        zoom *= 2;
    }

    public void zoomOut(int x, int y) throws ZoomLimitException {

        if (zoom / 2 <= 0) throw new ZoomLimitException("Can't zoom further out (" + zoom + ")");

        center = new Complex(x - translateX, y - translateY).divide(zoom).add(z1);

        Complex c = z2.sub(z1);

        z1 = center.sub(c);
        z2 = center.add(c);

        zoomPower--;
        zoom /= 2;
    }

    public Complex getZ1() {
        return z1;
    }

    public Complex getZ2() {
        return z2;
    }

    public Complex getCenter() {
        return center;
    }

    public int getZoomPower() {
        return zoomPower;
    }

    public long getZoom() {
        return zoom;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getTranslateX() {
        return translateX;
    }

    public int getTranslateY() {
        return translateY;
    }
}
