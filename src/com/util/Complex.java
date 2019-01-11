package com.util;

public class Complex {

    public static final Complex ZERO = new Complex(0, 0);

    public final double re, im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex add(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex sub(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex multiply(double d) {
        return new Complex(re * d, im * d);
    }

    public Complex divide(double d) {
        return new Complex(re / d, im / d);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Complex) {
            Complex other = (Complex) obj;
            return re == other.re && im == other.im;
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("%s; %s", re, im);
    }

    public static Complex parseComplex(String s) throws NumberFormatException {

        String[] components = s.split("\\s*;\\s*");

        if (components.length != 2)
            throw new NumberFormatException("Cannot parse '" + s + "' as a complex.");

        double re = Double.parseDouble(components[0]);
        double im = Double.parseDouble(components[1]);

        return new Complex(re, im);
    }
}
