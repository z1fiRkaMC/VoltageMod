package org.voltagemod.core.physics.electrical;

/**
 * Short-circuit calculations for AC electrical systems.
 *
 * All voltages and currents are RMS phasors.
 * All impedances are represented in complex form.
 */
public final class ShortCircuitCalculations {

    private static final double SQRT_THREE = Math.sqrt(3.0);

    private ShortCircuitCalculations() {
    }

    /**
     * Calculates single-phase short-circuit current.
     *
     * I_k = U_ph / (Z_loop + Z_f)
     *
     * Z_loop includes the complete phase and return path.
     * Z_f is the fault impedance.
     */
    public static Complex calculateSinglePhaseShortCircuitCurrent(
            Complex phaseVoltage,
            Complex loopImpedance,
            Complex faultImpedance
    ) {
        validatePhasor(phaseVoltage, "Phase voltage");
        validateImpedance(loopImpedance, "Loop impedance");
        validateImpedance(faultImpedance, "Fault impedance");

        Complex totalImpedance =
                loopImpedance.add(faultImpedance);

        validateNonZeroImpedance(totalImpedance);

        return phaseVoltage.divide(totalImpedance);
    }

    /**
     * Calculates single-phase short-circuit current magnitude.
     */
    public static double calculateSinglePhaseShortCircuitCurrentMagnitude(
            Complex phaseVoltage,
            Complex loopImpedance,
            Complex faultImpedance
    ) {
        return calculateSinglePhaseShortCircuitCurrent(
                phaseVoltage,
                loopImpedance,
                faultImpedance
        ).magnitude();
    }

    /**
     * Calculates balanced three-phase short-circuit current.
     *
     * I_k = (U_line / sqrt(3)) / (Z_1 + Z_f)
     *
     * Z_1 is the equivalent positive-sequence
     * Thevenin impedance per phase.
     * Z_f is the fault impedance per phase.
     */
    public static Complex calculateThreePhaseShortCircuitCurrent(
            double lineVoltage,
            Complex positiveSequenceImpedance,
            Complex faultImpedance
    ) {
        validateNonNegativeFinite(lineVoltage, "Line voltage");
        validateImpedance(
                positiveSequenceImpedance,
                "Positive-sequence impedance"
        );
        validateImpedance(faultImpedance, "Fault impedance");

        Complex totalImpedance =
                positiveSequenceImpedance.add(faultImpedance);

        validateNonZeroImpedance(totalImpedance);

        double phaseVoltage = lineVoltage / SQRT_THREE;

        Complex voltagePhasor = new Complex(
                phaseVoltage,
                0.0
        );

        return voltagePhasor.divide(totalImpedance);
    }

    /**
     * Calculates three-phase short-circuit current magnitude.
     */
    public static double calculateThreePhaseShortCircuitCurrentMagnitude(
            double lineVoltage,
            Complex positiveSequenceImpedance,
            Complex faultImpedance
    ) {
        return calculateThreePhaseShortCircuitCurrent(
                lineVoltage,
                positiveSequenceImpedance,
                faultImpedance
        ).magnitude();
    }

    /**
     * Calculates initial three-phase short-circuit apparent power.
     *
     * S_k = sqrt(3) * U_line * I_k
     *
     * This is a short-circuit apparent power magnitude,
     * not the active power dissipated at the fault.
     */
    public static double calculateThreePhaseShortCircuitPower(
            double lineVoltage,
            double shortCircuitCurrent
    ) {
        validateNonNegativeFinite(lineVoltage, "Line voltage");
        validateNonNegativeFinite(
                shortCircuitCurrent,
                "Short-circuit current"
        );

        return SQRT_THREE * lineVoltage * shortCircuitCurrent;
    }

    private static void validatePhasor(
            Complex value,
            String name
    ) {
        if (value == null) {
            throw new IllegalArgumentException(
                    name + " cannot be null."
            );
        }
    }

    private static void validateImpedance(
            Complex impedance,
            String name
    ) {
        validatePhasor(impedance, name);
    }

    private static void validateNonZeroImpedance(
            Complex impedance
    ) {
        if (impedance.getReal() == 0.0
                && impedance.getImaginary() == 0.0) {

            throw new ArithmeticException(
                    "Total impedance cannot be zero."
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