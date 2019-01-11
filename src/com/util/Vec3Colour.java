package com.util;

import java.awt.*;

public class Vec3Colour {

    private final float r;
    private final float g;
    private final float b;

    public Vec3Colour(Color c) {
        r = c.getRed() / 255f;
        g = c.getGreen() / 255f;
        b = c.getBlue() / 255f;
    }

    public Vec3Colour(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Vec3Colour add(Vec3Colour c) {
        return new Vec3Colour(r + c.r, g + c.g, b + c.b);
    }

    public Vec3Colour multiply(float f) {
        return new Vec3Colour(r * f, g * f, b * f);
    }

    private static int toInt(float f) {
        if (f > 1)
            return 255;
        if (f < 0)
            return 0;
        return (int) (255 * f);
    }

    public int getRGB() {
        int r = toInt(this.r);
        int g = toInt(this.g);
        int b = toInt(this.b);

        return b | (g << 8) | (r << 16) | (255 << 24);
    }
}
