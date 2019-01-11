package com.util;

import java.awt.*;

public class Turtle {

    private double x;
    private double y;

    private double heading;

    private boolean penDown;

    private final Graphics g;

    public Turtle(Graphics g, double x, double y) {

        this.x = x;
        this.y = y;

        heading = 0;

        penDown = true;

        this.g = g;
    }

    public void forward(double length) {
        setXY(x + length * Math.sin(Math.toRadians(heading)), y + length * Math.cos(Math.toRadians(heading)));
    }

    public void back(double length) {
        forward(-length);
    }

    public void left(double angle) {
        setHeading(heading + angle);
    }

    public void right(double angle) {
        setHeading(heading - angle);
    }


    public void setXY(double x, double y) {
        if (penDown) g.drawLine(round(this.x), round(this.y), round(x), round(y));

        this.x = x;
        this.y = y;
    }

    public void setHeading(double angle) {
        heading = angle % 360;
    }

    public void setPenDown(boolean penDown) {
        this.penDown = penDown;
    }


    public int round(double d) {
        return (int) Math.round(d);
    }


    public void drawPolygon(double sideLength, int sides) {
        for (int i = sides; i > 0; i--) {
            forward(sideLength);
            left(360d / sides);
        }
    }

    public void fillPolygon(double sideLength, int sides) {

        boolean temp = penDown;
        setPenDown(false);
        int[] xArray = new int[sides];
        int[] yArray = new int[sides];

        for (int i = sides; i > 0; i--) {
            xArray[i - 1] = round(x);
            yArray[i - 1] = round(y);
            forward(sideLength);
            left(360d / sides);
        }

        g.fillPolygon(xArray, yArray, sides);

        setPenDown(temp);
    }
}
