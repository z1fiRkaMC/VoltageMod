package org.voltagemod.core.physics.electrical;

/**
 * Calculations for unbalanced three-phase electrical systems.
 *
 * Phase order: L1, L2, L3.
 * All voltages and currents are represented as complex phasors.
 */
public final class UnbalancedThreePhaseCalculations {

    private static final int PHASE_COUNT = 3;

    private UnbalancedThreePhaseCalculations() {
    }

    /**
     * Calculates complex power of one phase.
     * S = U * conjugate(I)
     */
    public static Complex calculatePhaseComplexPower(
            Complex voltage,
            Complex current
    ) {
        if (voltage == null || current == null) {
            throw new IllegalArgumentException(
                    "Voltage and current cannot be null."
            );
        }

        return PowerCalculations.calculateComplexPower(
                voltage,
                current
        );
    }

    /**
     * Calculates complex power for all three phases.
     */
    public static Complex calculateTotalComplexPower(
            Complex[] voltages,
            Complex[] currents
    ) {
        validatePhases(voltages, "Voltages");
        validatePhases(currents, "Currents");

        Complex totalPower = Complex.ZERO;

        for (int i = 0; i < PHASE_COUNT; i++) {
            Complex phasePower = calculatePhaseComplexPower(
                    voltages[i],
                    currents[i]
            );

            totalPower = totalPower.add(phasePower);
        }

        return totalPower;
    }

    /**
     * Total active power.
     * P = Re(S_total)
     */
    public static double calculateTotalActivePower(
            Complex[] voltages,
            Complex[] currents
    ) {
        return calculateTotalComplexPower(
                voltages,
                currents
        ).getReal();
    }

    /**
     * Total reactive power.
     * Q = Im(S_total)
     */
    public static double calculateTotalReactivePower(
            Complex[] voltages,
            Complex[] currents
    ) {
        return calculateTotalComplexPower(
                voltages,
                currents
        ).getImaginary();
    }

    /**
     * Total apparent power.
     * S = |S_total|
     */
    public static double calculateTotalApparentPower(
            Complex[] voltages,
            Complex[] currents
    ) {
        return calculateTotalComplexPower(
                voltages,
                currents
        ).magnitude();
    }

    /**
     * Sum of individual phase apparent powers.
     *
     * This value can differ from the magnitude of total complex power.
     */
    public static double calculateSumOfPhaseApparentPowers(
            Complex[] voltages,
            Complex[] currents
    ) {
        validatePhases(voltages, "Voltages");
        validatePhases(currents, "Currents");

        double total = 0.0;

        for (int i = 0; i < PHASE_COUNT; i++) {
            Complex phasePower = calculatePhaseComplexPower(
                    voltages[i],
                    currents[i]
            );

            total += phasePower.magnitude();
        }

        return total;
    }

    /**
     * Calculates neutral current phasor for a star-connected system.
     *
     * In = -(I1 + I2 + I3)
     */
    public static Complex calculateNeutralCurrent(
            Complex[] phaseCurrents
    ) {
        validatePhases(phaseCurrents, "Phase currents");

        Complex currentSum = Complex.ZERO;

        for (int i = 0; i < PHASE_COUNT; i++) {
            currentSum = currentSum.add(phaseCurrents[i]);
        }

        return currentSum.negate();
    }

    /**
     * Calculates neutral current magnitude.
     */
    public static double calculateNeutralCurrentMagnitude(
            Complex[] phaseCurrents
    ) {
        return calculateNeutralCurrent(phaseCurrents).magnitude();
    }

    private static void validatePhases(
            Complex[] phases,
            String name
    ) {
        if (phases == null || phases.length != PHASE_COUNT) {
            throw new IllegalArgumentException(
                    name + " must contain exactly three phases."
            );
        }

        for (int i = 0; i < PHASE_COUNT; i++) {
            if (phases[i] == null) {
                throw new IllegalArgumentException(
                        name + " cannot contain null elements."
                );
            }
        }
    }
}