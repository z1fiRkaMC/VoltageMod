package org.voltagemod.core.physics.electrical;

/**
 * Power calculations for balanced three-phase loads.
 */
public final class ThreePhaseLoadCalculations {

    private ThreePhaseLoadCalculations() {
    }

    // =========================
    // STAR CONNECTION (Y)
    // =========================

    /**
     * P = sqrt(3) * U_line * I_line * PF
     */
    public static double calculateStarActivePower(
            double lineVoltage,
            double lineCurrent,
            double powerFactor
    ) {
        validate(lineVoltage, lineCurrent, powerFactor);

        return Math.sqrt(3.0) * lineVoltage * lineCurrent * powerFactor;
    }

    /**
     * Q = sqrt(3) * U_line * I_line * sin(phi)
     */
    public static double calculateStarReactivePower(
            double lineVoltage,
            double lineCurrent,
            double powerFactor
    ) {
        validate(lineVoltage, lineCurrent, powerFactor);

        double sinPhi = Math.sqrt(1.0 - powerFactor * powerFactor);

        return Math.sqrt(3.0) * lineVoltage * lineCurrent * sinPhi;
    }

    /**
     * S = sqrt(3) * U_line * I_line
     */
    public static double calculateStarApparentPower(
            double lineVoltage,
            double lineCurrent
    ) {
        validateNonNegativeFinite(lineVoltage, "Line voltage");
        validateNonNegativeFinite(lineCurrent, "Line current");

        return Math.sqrt(3.0) * lineVoltage * lineCurrent;
    }

    // =========================
    // DELTA CONNECTION (Δ)
    // =========================

    /**
     * P = sqrt(3) * U_line * I_line * PF
     */
    public static double calculateDeltaActivePower(
            double lineVoltage,
            double lineCurrent,
            double powerFactor
    ) {
        validate(lineVoltage, lineCurrent, powerFactor);

        return Math.sqrt(3.0) * lineVoltage * lineCurrent * powerFactor;
    }

    /**
     * Q = sqrt(3) * U_line * I_line * sin(phi)
     */
    public static double calculateDeltaReactivePower(
            double lineVoltage,
            double lineCurrent,
            double powerFactor
    ) {
        validate(lineVoltage, lineCurrent, powerFactor);

        double sinPhi = Math.sqrt(1.0 - powerFactor * powerFactor);

        return Math.sqrt(3.0) * lineVoltage * lineCurrent * sinPhi;
    }

    /**
     * S = sqrt(3) * U_line * I_line
     */
    public static double calculateDeltaApparentPower(
            double lineVoltage,
            double lineCurrent
    ) {
        validateNonNegativeFinite(lineVoltage, "Line voltage");
        validateNonNegativeFinite(lineCurrent, "Line current");

        return Math.sqrt(3.0) * lineVoltage * lineCurrent;
    }

    // =========================
    // POWER FACTOR
    // =========================

    /**
     * PF = P / S
     */
    public static double calculatePowerFactor(
            double activePower,
            double apparentPower
    ) {
        validateNonNegativeFinite(activePower, "Active power");
        validateNonNegativeFinite(apparentPower, "Apparent power");

        if (apparentPower == 0.0) {
            throw new ArithmeticException(
                    "Apparent power cannot be zero."
            );
        }

        return activePower / apparentPower;
    }

    // =========================
    // VALIDATION
    // =========================

    private static void validate(
            double voltage,
            double current,
            double powerFactor
    ) {
        validateNonNegativeFinite(voltage, "Line voltage");
        validateNonNegativeFinite(current, "Line current");

        if (Double.isNaN(powerFactor)
                || Double.isInfinite(powerFactor)
                || powerFactor < 0.0
                || powerFactor > 1.0) {

            throw new IllegalArgumentException(
                    "Power factor must be between 0 and 1."
            );
        }
    }

    private static void validateNonNegativeFinite(
            double value,
            String name
    ) {
        if (Double.isNaN(value)
                || Double.isInfinite(value)
                || value < 0.0) {

            throw new IllegalArgumentException(
                    name + " must be finite and non-negative."
            );
        }
    }
}