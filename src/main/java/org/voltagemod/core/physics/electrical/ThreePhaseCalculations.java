package org.voltagemod.core.physics.electrical;

public final class ThreePhaseCalculations {

    private static final double PHASE_SHIFT =
            2.0 * Math.PI / 3.0;

    private ThreePhaseCalculations() {
    }

    /**
     * Creates a balanced three-phase voltage system.
     *
     * L1 = U ∠ 0°
     * L2 = U ∠ -120°
     * L3 = U ∠ +120°
     *
     * All voltages are RMS values.
     */
    public static Complex[] createBalancedPhaseVoltages(
            double phaseVoltage,
            PhaseAngle referenceAngle
    ) {
        requireNonNegative(phaseVoltage, "Phase voltage");

        if (referenceAngle == null) {
            throw new IllegalArgumentException(
                    "Reference angle cannot be null."
            );
        }

        double angle = referenceAngle.getRadians();

        return new Complex[]{
                Complex.fromPolar(phaseVoltage, angle),
                Complex.fromPolar(phaseVoltage, angle - PHASE_SHIFT),
                Complex.fromPolar(phaseVoltage, angle + PHASE_SHIFT)
        };
    }

    /**
     * Calculates line voltage between two phases.
     *
     * Uab = Ua - Ub
     */
    public static Complex calculateLineVoltage(
            Complex firstPhase,
            Complex secondPhase
    ) {
        requireNotNull(firstPhase, "First phase");
        requireNotNull(secondPhase, "Second phase");

        return firstPhase.subtract(secondPhase);
    }

    /**
     * Calculates line voltage magnitude
     * in a balanced star-connected system.
     *
     * Uline = sqrt(3) * Uphase
     */
    public static double calculateLineVoltageMagnitude(
            double phaseVoltage
    ) {
        requireNonNegative(phaseVoltage, "Phase voltage");

        return Math.sqrt(3.0) * phaseVoltage;
    }

    /**
     * Calculates the vector sum of phase quantities.
     */
    public static Complex calculatePhaseVectorSum(
            Complex[] phases
    ) {
        if (phases == null || phases.length == 0) {
            throw new IllegalArgumentException(
                    "Phase array cannot be empty."
            );
        }

        Complex result = Complex.ZERO;

        for (Complex phase : phases) {
            requireNotNull(phase, "Phase");

            result = result.add(phase);
        }

        return result;
    }

    /**
     * Checks whether a three-phase system is balanced.
     *
     * The expected phase sequence is:
     * L1 = 0°
     * L2 = -120°
     * L3 = +120°
     *
     * Magnitude tolerance is relative.
     * Angle tolerance is measured in radians.
     */
    public static boolean isBalancedSystem(
            Complex[] phases,
            double magnitudeTolerance,
            double angleToleranceRadians
    ) {
        if (phases == null || phases.length != 3) {
            return false;
        }

        if (!Double.isFinite(magnitudeTolerance)
                || magnitudeTolerance < 0.0
                || !Double.isFinite(angleToleranceRadians)
                || angleToleranceRadians < 0.0) {
            throw new IllegalArgumentException(
                    "Tolerances must be finite and non-negative."
            );
        }

        for (Complex phase : phases) {
            if (phase == null) {
                return false;
            }
        }

        double magnitudeA = phases[0].magnitude();
        double magnitudeB = phases[1].magnitude();
        double magnitudeC = phases[2].magnitude();

        double maxMagnitude = Math.max(
                magnitudeA,
                Math.max(magnitudeB, magnitudeC)
        );

        if (maxMagnitude == 0.0) {
            return false;
        }

        if (Math.abs(magnitudeA - magnitudeB)
                > maxMagnitude * magnitudeTolerance
                || Math.abs(magnitudeA - magnitudeC)
                > maxMagnitude * magnitudeTolerance) {
            return false;
        }

        double angleAB = new PhaseAngle(
                phases[1].argument() - phases[0].argument()
        ).getRadians();

        double angleAC = new PhaseAngle(
                phases[2].argument() - phases[0].argument()
        ).getRadians();

        double expectedAB = -PHASE_SHIFT;
        double expectedAC = PHASE_SHIFT;

        double differenceAB = new PhaseAngle(
                angleAB - expectedAB
        ).getRadians();

        double differenceAC = new PhaseAngle(
                angleAC - expectedAC
        ).getRadians();

        return Math.abs(differenceAB) <= angleToleranceRadians
                && Math.abs(differenceAC) <= angleToleranceRadians;
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

    private static void requireNotNull(
            Complex value,
            String name
    ) {
        if (value == null) {
            throw new IllegalArgumentException(
                    name + " cannot be null."
            );
        }
    }
}