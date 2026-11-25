package org.voltagemod.core.physics.electrical;

/**
 * Voltage drop calculations for electrical transmission lines.
 *
 * AC calculations use the approximate voltage drop formulas
 * for balanced loads.
 */
public final class VoltageDropCalculations {

    private static final double SQRT_THREE = Math.sqrt(3.0);

    private VoltageDropCalculations() {
    }

    /**
     * DC voltage drop:
     * ΔU = I * R
     */
    public static double calculateDcVoltageDrop(
            double current,
            double resistance
    ) {
        validateNonNegativeFinite(current, "Current");
        validateNonNegativeFinite(resistance, "Resistance");

        return current * resistance;
    }

    /**
     * Single-phase AC voltage drop:
     * ΔU = 2 * I * (R * cos(phi) + X * sin(phi))
     *
     * Resistance and reactance are specified per conductor.
     * Positive phase angle represents an inductive load.
     * Negative phase angle represents a capacitive load.
     */
    public static double calculateSinglePhaseAcVoltageDrop(
            double current,
            double resistance,
            double reactance,
            double phaseAngleRadians
    ) {
        validateAcParameters(
                current,
                resistance,
                reactance,
                phaseAngleRadians
        );

        double voltageDrop =
                resistance * Math.cos(phaseAngleRadians)
                        + reactance * Math.sin(phaseAngleRadians);

        return 2.0 * current * voltageDrop;
    }

    /**
     * Balanced three-phase AC voltage drop:
     * ΔU = sqrt(3) * I * (R * cos(phi) + X * sin(phi))
     *
     * Resistance and reactance are specified per phase conductor.
     */
    public static double calculateThreePhaseAcVoltageDrop(
            double current,
            double resistance,
            double reactance,
            double phaseAngleRadians
    ) {
        validateAcParameters(
                current,
                resistance,
                reactance,
                phaseAngleRadians
        );

        double voltageDrop =
                resistance * Math.cos(phaseAngleRadians)
                        + reactance * Math.sin(phaseAngleRadians);

        return SQRT_THREE * current * voltageDrop;
    }

    /**
     * Percentage voltage drop:
     * ΔU% = (ΔU / U) * 100
     */
    public static double calculateVoltageDropPercentage(
            double voltageDrop,
            double nominalVoltage
    ) {
        validateFinite(voltageDrop, "Voltage drop");
        validateNonNegativeFinite(nominalVoltage, "Nominal voltage");

        if (nominalVoltage == 0.0) {
            throw new ArithmeticException(
                    "Nominal voltage cannot be zero."
            );
        }

        return (voltageDrop / nominalVoltage) * 100.0;
    }

    private static void validateAcParameters(
            double current,
            double resistance,
            double reactance,
            double phaseAngleRadians
    ) {
        validateNonNegativeFinite(current, "Current");
        validateNonNegativeFinite(resistance, "Resistance");
        validateNonNegativeFinite(reactance, "Reactance");
        validateFinite(phaseAngleRadians, "Phase angle");
    }

    private static void validateNonNegativeFinite(
            double value,
            String name
    ) {
        validateFinite(value, name);

        if (value < 0.0) {
            throw new IllegalArgumentException(
                    name + " must be non-negative."
            );
        }
    }

    private static void validateFinite(
            double value,
            String name
    ) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(
                    name + " must be finite."
            );
        }
    }
}