package org.voltagemod.core.physics.electrical;

/**
 * Power factor correction calculations.
 *
 * All electrical values use SI units.
 * Voltages are RMS values.
 * Capacitance is returned in farads.
 */
public final class PowerFactorCorrection {

    private static final double TWO_PI = 2.0 * Math.PI;

    private PowerFactorCorrection() {
    }

    /**
     * Calculates the initial phase angle from power factor.
     *
     * phi = acos(PF)
     */
    public static double calculatePhaseAngle(double powerFactor) {
        validatePowerFactor(powerFactor);

        return Math.acos(powerFactor);
    }

    /**
     * Calculates the required reactive compensation power.
     *
     * Qc = P * (tan(phi1) - tan(phi2))
     */
    public static double calculateRequiredReactivePower(
            double activePower,
            double initialPowerFactor,
            double targetPowerFactor
    ) {
        validateNonNegativeFinite(activePower, "Active power");
        validatePowerFactor(initialPowerFactor);
        validatePowerFactor(targetPowerFactor);

        if (targetPowerFactor < initialPowerFactor) {
            throw new IllegalArgumentException(
                    "Target power factor cannot be lower than initial power factor."
            );
        }

        if (activePower == 0.0
                || targetPowerFactor == initialPowerFactor) {
            return 0.0;
        }

        double initialAngle = Math.acos(initialPowerFactor);
        double targetAngle = Math.acos(targetPowerFactor);

        double initialTangent = Math.tan(initialAngle);
        double targetTangent = Math.tan(targetAngle);

        double compensation =
                activePower * (initialTangent - targetTangent);

        if (Double.isNaN(compensation)
                || Double.isInfinite(compensation)) {
            throw new ArithmeticException(
                    "Reactive compensation result is not finite."
            );
        }

        return compensation;
    }

    /**
     * Calculates the resulting power factor after compensation.
     *
     * PF = P / sqrt(P^2 + Q^2)
     */
    public static double calculateCorrectedPowerFactor(
            double activePower,
            double initialPowerFactor,
            double compensationPower
    ) {
        validateNonNegativeFinite(activePower, "Active power");
        validatePowerFactor(initialPowerFactor);
        validateNonNegativeFinite(
                compensationPower,
                "Compensation power"
        );

        if (activePower == 0.0) {
            throw new IllegalArgumentException(
                    "Active power must be greater than zero."
            );
        }

        double initialAngle = Math.acos(initialPowerFactor);

        double initialReactivePower =
                activePower * Math.tan(initialAngle);

        double remainingReactivePower =
                initialReactivePower - compensationPower;

        double apparentPower = Math.hypot(
                activePower,
                remainingReactivePower
        );

        return activePower / apparentPower;
    }

    /**
     * Calculates single-phase capacitor bank capacitance.
     *
     * Qc = 2 * PI * f * C * U^2
     */
    public static double calculateSinglePhaseCapacitance(
            double reactivePower,
            double frequency,
            double voltage
    ) {
        validateNonNegativeFinite(
                reactivePower,
                "Reactive power"
        );
        validatePositiveFinite(frequency, "Frequency");
        validatePositiveFinite(voltage, "Voltage");

        if (reactivePower == 0.0) {
            return 0.0;
        }

        double capacitance =
                reactivePower / (TWO_PI * frequency * voltage * voltage);

        validateFiniteResult(capacitance);

        return capacitance;
    }

    /**
     * Calculates three-phase star-connected capacitor bank capacitance.
     *
     * C_Y = Qc / (2 * PI * f * U_line^2)
     */
    public static double calculateThreePhaseStarCapacitance(
            double reactivePower,
            double frequency,
            double lineVoltage
    ) {
        return calculateSinglePhaseCapacitance(
                reactivePower,
                frequency,
                lineVoltage
        );
    }

    /**
     * Calculates three-phase delta-connected capacitor bank capacitance.
     *
     * C_Delta = Qc / (3 * 2 * PI * f * U_line^2)
     */
    public static double calculateThreePhaseDeltaCapacitance(
            double reactivePower,
            double frequency,
            double lineVoltage
    ) {
        validateNonNegativeFinite(
                reactivePower,
                "Reactive power"
        );
        validatePositiveFinite(frequency, "Frequency");
        validatePositiveFinite(lineVoltage, "Line voltage");

        if (reactivePower == 0.0) {
            return 0.0;
        }

        double capacitance =
                reactivePower
                        / (3.0 * TWO_PI * frequency
                        * lineVoltage * lineVoltage);

        validateFiniteResult(capacitance);

        return capacitance;
    }

    private static void validatePowerFactor(double powerFactor) {
        if (Double.isNaN(powerFactor)
                || Double.isInfinite(powerFactor)
                || powerFactor <= 0.0
                || powerFactor > 1.0) {

            throw new IllegalArgumentException(
                    "Power factor must be greater than zero and not greater than one."
            );
        }
    }

    private static void validatePositiveFinite(
            double value,
            String name
    ) {
        if (Double.isNaN(value)
                || Double.isInfinite(value)
                || value <= 0.0) {

            throw new IllegalArgumentException(
                    name + " must be finite and greater than zero."
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

    private static void validateFiniteResult(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new ArithmeticException(
                    "Calculation result is not finite."
            );
        }
    }
}