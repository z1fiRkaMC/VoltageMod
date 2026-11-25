package org.voltagemod.core.physics.electrical;

public final class ACCalculations {

    private ACCalculations() {
    }

    public static double calculateRmsVoltage(double peakVoltage) {
        requireNonNegative(peakVoltage, "Peak voltage");

        return peakVoltage / Math.sqrt(2.0);
    }

    public static double calculateRmsCurrent(double peakCurrent) {
        requireNonNegative(peakCurrent, "Peak current");

        return peakCurrent / Math.sqrt(2.0);
    }

    public static double calculateActivePower(
            double voltage,
            double current,
            double powerFactor
    ) {
        validateRmsValues(voltage, current);
        requirePowerFactor(powerFactor);

        return voltage * current * powerFactor;
    }

    public static double calculateReactivePower(
            double voltage,
            double current,
            double phaseAngleRadians
    ) {
        validateRmsValues(voltage, current);

        if (!Double.isFinite(phaseAngleRadians)) {
            throw new IllegalArgumentException(
                    "Phase angle must be finite."
            );
        }

        return voltage * current * Math.sin(phaseAngleRadians);
    }

    public static double calculateApparentPower(
            double voltage,
            double current
    ) {
        validateRmsValues(voltage, current);

        return voltage * current;
    }

    public static double calculatePowerFactor(
            double activePower,
            double apparentPower
    ) {
        if (!Double.isFinite(activePower)) {
            throw new IllegalArgumentException(
                    "Active power must be finite."
            );
        }

        requireNonNegative(apparentPower, "Apparent power");

        if (apparentPower == 0.0) {
            throw new ArithmeticException(
                    "Apparent power cannot be zero."
            );
        }

        double powerFactor = activePower / apparentPower;

        requirePowerFactor(powerFactor);

        return powerFactor;
    }

    private static void validateRmsValues(
            double voltage,
            double current
    ) {
        requireNonNegative(voltage, "RMS voltage");
        requireNonNegative(current, "RMS current");
    }

    private static void requireNonNegative(
            double value,
            String name
    ) {
        if (!Double.isFinite(value) || value < 0.0) {
            throw new IllegalArgumentException(
                    name + " must be finite and non-negative."
            );
        }
    }

    private static void requirePowerFactor(double value) {
        if (!Double.isFinite(value) || value < -1.0 || value > 1.0) {
            throw new IllegalArgumentException(
                    "Power factor must be between -1 and 1."
            );
        }
    }
}
