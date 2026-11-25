package org.voltagemod.core.physics.electrical;

public final class Complex {

    public static final Complex ZERO = new Complex(0.0, 0.0);
    public static final Complex ONE = new Complex(1.0, 0.0);

    private final double real;
    private final double imaginary;

    public Complex(double real, double imaginary) {
        if (!Double.isFinite(real) || !Double.isFinite(imaginary)) {
            throw new IllegalArgumentException(
                    "Complex components must be finite."
            );
        }

        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex add(Complex other) {
        return new Complex(
                real + other.real,
                imaginary + other.imaginary
        );
    }

    public Complex subtract(Complex other) {
        return new Complex(
                real - other.real,
                imaginary - other.imaginary
        );
    }

    public Complex multiply(Complex other) {
        return new Complex(
                real * other.real - imaginary * other.imaginary,
                real * other.imaginary + imaginary * other.real
        );
    }

    public Complex divide(Complex other) {
        double denominator =
                other.real * other.real +
                other.imaginary * other.imaginary;

        if (denominator == 0.0) {
            throw new ArithmeticException(
                    "Division by zero complex number."
            );
        }

        return new Complex(
                (real * other.real + imaginary * other.imaginary)
                        / denominator,
                (imaginary * other.real - real * other.imaginary)
                        / denominator
        );
    }

    public Complex conjugate() {
        return new Complex(real, -imaginary);
    }

    public double magnitude() {
        return Math.hypot(real, imaginary);
    }

    public double argument() {
        return Math.atan2(imaginary, real);
    }

    public Complex negate() {
        return new Complex(-real, -imaginary);
    }

    public static Complex fromPolar(double magnitude, double angle) {
        if (!Double.isFinite(magnitude) || magnitude < 0.0
                || !Double.isFinite(angle)) {
            throw new IllegalArgumentException(
                    "Invalid polar coordinates."
            );
        }

        return new Complex(
                magnitude * Math.cos(angle),
                magnitude * Math.sin(angle)
        );
    }

    @Override
    public String toString() {
        if (imaginary == 0.0) {
            return Double.toString(real);
        }

        if (real == 0.0) {
            return imaginary + "i";
        }

        return real + (imaginary >= 0.0 ? " + " : " - ")
                + Math.abs(imaginary) + "i";
    }
}
