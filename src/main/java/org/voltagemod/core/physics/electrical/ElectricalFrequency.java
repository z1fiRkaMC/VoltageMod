package org.voltagemod.core.physics.electrical;

public final class ElectricalFrequency {

    private ElectricalFrequency() {
    }

    /**
     * Calculates angular frequency
     *
     * ω = 2πf
     *
     * @param frequency frequency in Hz
     * @return angular frequency in rad/s
     */
    public static double calculateAngularFrequency(double frequency) {
        requireNonNegative(frequency, "Frequency");

        double result = 2.0 * Math.PI * frequency;

        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Angular frequency exceeds the supported range."
            );
        }

        return result;
    }

    /**
     * Calculates ordinary frequency
     *
     * f = ω / 2π
     *
     * @param angularFrequency angular frequency in rad/s
     * @return frequency in Hz
     */
    public static double calculateFrequency(double angularFrequency) {
        requireNonNegative(angularFrequency, "Angular frequency");

        return angularFrequency / (2.0 * Math.PI);
    }

    private static void requireNonNegative(double value, String name) {
        if (!Double.isFinite(value) || value < 0.0) {
            throw new IllegalArgumentException(
                    name + " must be finite and non-negative."
            );
        }
    }
}
