package org.voltagemod.core.physics.electrical;

public final class ThreePhasePowerCalculations {

    private ThreePhasePowerCalculations() {
    }

    /**
     * Calculates complex power of one phase.
     *
     * S = U * conjugate(I)
     */
    public static Complex calculatePhasePower(
            Complex voltage,
            Complex current
    ) {
        return PowerCalculations.calculateComplexPower(
                voltage,
                current
        );
    }

    /**
     * Calculates total complex power of three phases.
     *
     * Stotal = S1 + S2 + S3
     */
    public static Complex calculateTotalComplexPower(
            Complex[] phaseVoltages,
            Complex[] phaseCurrents
    ) {
        validatePhases(phaseVoltages, phaseCurrents);

        Complex totalPower = Complex.ZERO;

        for (int i = 0; i < 3; i++) {
            Complex phasePower = calculatePhasePower(
                    phaseVoltages[i],
                    phaseCurrents[i]
            );

            totalPower = totalPower.add(phasePower);
        }

        return totalPower;
    }

    /**
     * Returns total active power in watts.
     */
    public static double calculateTotalActivePower(
            Complex[] phaseVoltages,
            Complex[] phaseCurrents
    ) {
        return calculateTotalComplexPower(
                phaseVoltages,
                phaseCurrents
        ).getReal();
    }

    /**
     * Returns total reactive power in var.
     */
    public static double calculateTotalReactivePower(
            Complex[] phaseVoltages,
            Complex[] phaseCurrents
    ) {
        return calculateTotalComplexPower(
                phaseVoltages,
                phaseCurrents
        ).getImaginary();
    }

    /**
     * Returns total apparent power in VA.
     *
     * This is the magnitude of total complex power.
     */
    public static double calculateTotalApparentPower(
            Complex[] phaseVoltages,
            Complex[] phaseCurrents
    ) {
        return calculateTotalComplexPower(
                phaseVoltages,
                phaseCurrents
        ).magnitude();
    }

    /**
     * Calculates sum of individual phase apparent powers.
     *
     * This is not necessarily equal to the magnitude
     * of total complex power in an unbalanced system.
     */
    public static double calculateSumOfPhaseApparentPowers(
            Complex[] phaseVoltages,
            Complex[] phaseCurrents
    ) {
        validatePhases(phaseVoltages, phaseCurrents);

        double total = 0.0;

        for (int i = 0; i < 3; i++) {
            total += calculatePhasePower(
                    phaseVoltages[i],
                    phaseCurrents[i]
            ).magnitude();
        }

        return total;
    }

    /**
     * Calculates power factor of the complete system.
     */
    public static double calculateTotalPowerFactor(
            Complex[] phaseVoltages,
            Complex[] phaseCurrents
    ) {
        Complex totalPower = calculateTotalComplexPower(
                phaseVoltages,
                phaseCurrents
        );

        double apparentPower = totalPower.magnitude();

        if (apparentPower == 0.0) {
            throw new ArithmeticException(
                    "Power factor is undefined when apparent power is zero."
            );
        }

        return totalPower.getReal() / apparentPower;
    }

    private static void validatePhases(
            Complex[] voltages,
            Complex[] currents
    ) {
        if (voltages == null || currents == null
                || voltages.length != 3
                || currents.length != 3) {
            throw new IllegalArgumentException(
                    "Exactly three phase voltages and currents are required."
            );
        }

        for (int i = 0; i < 3; i++) {
            if (voltages[i] == null || currents[i] == null) {
                throw new IllegalArgumentException(
                        "Phase voltage and current cannot be null."
                );
            }
        }
    }
}