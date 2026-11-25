package org.voltagemod.core.physics.electrical;

/**
 * Electrical power and energy loss calculations.
 */
public final class ElectricalLossCalculations {

    private ElectricalLossCalculations() {
    }

    /**
     * P = I^2 * R
     */
    public static double calculatePowerLoss(
            double current,
            double resistance
    ) {
        validateNonNegativeFinite(current, "Current");
        validateNonNegativeFinite(resistance, "Resistance");

        return current * current * resistance;
    }

    /**
     * P = U^2 / R
     */
    public static double calculatePowerLossFromVoltage(
            double voltage,
            double resistance
    ) {
        validateNonNegativeFinite(voltage, "Voltage");
        validateNonNegativeFinite(resistance, "Resistance");

        if (resistance == 0.0) {
            throw new ArithmeticException(
                    "Resistance cannot be zero."
            );
        }

        return voltage * voltage / resistance;
    }

    /**
     * Single-phase line losses:
     * P_loss = 2 * I^2 * R
     *
     * Resistance is specified per conductor.
     */
    public static double calculateSinglePhaseLineLoss(
            double current,
            double conductorResistance
    ) {
        return 2.0 * calculatePowerLoss(
                current,
                conductorResistance
        );
    }

    /**
     * Balanced three-phase line losses:
     * P_loss = 3 * I^2 * R
     *
     * Resistance is specified per phase conductor.
     */
    public static double calculateThreePhaseLineLoss(
            double phaseCurrent,
            double phaseResistance
    ) {
        return 3.0 * calculatePowerLoss(
                phaseCurrent,
                phaseResistance
        );
    }

    /**
     * Energy loss:
     * E = P * t
     *
     * Time is specified in seconds.
     * Result is returned in joules.
     */
    public static double calculateEnergyLoss(
            double powerLoss,
            double timeSeconds
    ) {
        validateNonNegativeFinite(powerLoss, "Power loss");
        validateNonNegativeFinite(timeSeconds, "Time");

        return powerLoss * timeSeconds;
    }

    /**
     * Percentage power loss:
     * Loss% = (P_loss / P_input) * 100
     */
    public static double calculatePowerLossPercentage(
            double powerLoss,
            double inputPower
    ) {
        validateNonNegativeFinite(powerLoss, "Power loss");
        validateNonNegativeFinite(inputPower, "Input power");

        if (inputPower == 0.0) {
            throw new ArithmeticException(
                    "Input power cannot be zero."
            );
        }

        return (powerLoss / inputPower) * 100.0;
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