package org.voltagemod.core.physics.electrical;

public final class Impedance {

    private final Complex value;

    public Impedance(double resistance, double reactance) {
        this.value = new Complex(resistance, reactance);
    }

    public Impedance(Complex value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Impedance value cannot be null."
            );
        }

        this.value = value;
    }

    public Complex getValue() {
        return value;
    }

    public double getResistance() {
        return value.getReal();
    }

    public double getReactance() {
        return value.getImaginary();
    }

    public double getMagnitude() {
        return value.magnitude();
    }

    public double getPhaseRadians() {
        return value.argument();
    }

    public double getPhaseDegrees() {
        return Math.toDegrees(value.argument());
    }

    @Override
    public String toString() {
        return value + " Ω";
    }
}
