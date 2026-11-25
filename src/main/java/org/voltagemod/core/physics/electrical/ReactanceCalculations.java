package org.voltagemod.core.physics.electrical;

public final class ReactanceCalculations {

    private ReactanceCalculations() {
    }

    public static double calculateInductiveReactance(
            double frequency,
            double inductance
    ) {
        requireNonNegative(frequency, "Frequency");
        requireNonNegative(inductance, "Inductance");

        return 2.0 * Math.PI * frequency * inductance;
    }

    public static double calculateCapacitiveReactance(
            double frequency,
            double capacitance
    ) {
        requirePositive(frequency, "Frequency");
        requirePositive(capacitance, "Capacitance");

        return -1.0 / (2.0 * Math.PI * frequency * capacitance);
    }

    private static void requireNonNegative(double value, String name) {
        if (!Double.isFinite(value) || value < 0.0) {
            throw new IllegalArgumentException(
                    name + " must be finite and non-negative."
            );
        }
    }

    private static void requirePositive(double value, String name) {
        if (!Double.isFinite(value) || value <= 0.0) {
            throw new IllegalArgumentException(
                    name + " must be finite and greater than zero."
            );
        }
    }
}
