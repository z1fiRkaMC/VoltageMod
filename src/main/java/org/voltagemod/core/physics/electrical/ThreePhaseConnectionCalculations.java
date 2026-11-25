package org.voltagemod.core.physics.electrical;

/**
 * Calculations for balanced three-phase star (Y)
 * and delta (Δ) connections.
 *
 * All values represent magnitudes.
 */
public final class ThreePhaseConnectionCalculations {

    private static final double SQRT_THREE = Math.sqrt(3.0);

    private ThreePhaseConnectionCalculations() {
    }

    // =========================
    // STAR CONNECTION (Y)
    // =========================

    /**
     * U_line = sqrt(3) * U_phase
     */
    public static double calculateStarLineVoltage(double phaseVoltage) {
        requireNonNegativeFinite(phaseVoltage, "Phase voltage");
        return phaseVoltage * SQRT_THREE;
    }

    /**
     * U_phase = U_line / sqrt(3)
     */
    public static double calculateStarPhaseVoltage(double lineVoltage) {
        requireNonNegativeFinite(lineVoltage, "Line voltage");
        return lineVoltage / SQRT_THREE;
    }

    /**
     * I_line = I_phase
     */
    public static double calculateStarLineCurrent(double phaseCurrent) {
        requireNonNegativeFinite(phaseCurrent, "Phase current");
        return phaseCurrent;
    }

    /**
     * I_phase = I_line
     */
    public static double calculateStarPhaseCurrent(double lineCurrent) {
        requireNonNegativeFinite(lineCurrent, "Line current");
        return lineCurrent;
    }

    // =========================
    // DELTA CONNECTION (Δ)
    // =========================

    /**
     * U_line = U_phase
     */
    public static double calculateDeltaLineVoltage(double phaseVoltage) {
        requireNonNegativeFinite(phaseVoltage, "Phase voltage");
        return phaseVoltage;
    }

    /**
     * U_phase = U_line
     */
    public static double calculateDeltaPhaseVoltage(double lineVoltage) {
        requireNonNegativeFinite(lineVoltage, "Line voltage");
        return lineVoltage;
    }

    /**
     * I_line = sqrt(3) * I_phase
     */
    public static double calculateDeltaLineCurrent(double phaseCurrent) {
        requireNonNegativeFinite(phaseCurrent, "Phase current");
        return phaseCurrent * SQRT_THREE;
    }

    /**
     * I_phase = I_line / sqrt(3)
     */
    public static double calculateDeltaPhaseCurrent(double lineCurrent) {
        requireNonNegativeFinite(lineCurrent, "Line current");
        return lineCurrent / SQRT_THREE;
    }

    private static void requireNonNegativeFinite(
            double value,
            String name
    ) {
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0.0) {
            throw new IllegalArgumentException(
                    name + " must be finite and non-negative."
            );
        }
    }
}