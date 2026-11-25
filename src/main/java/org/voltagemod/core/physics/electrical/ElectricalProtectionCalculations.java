package org.voltagemod.core.physics.electrical;

/**
 * Electrical protection calculations.
 *
 * Current: amperes (A).
 * Time: seconds (s).
 * Cable cross-section: square millimeters (mm^2).
 *
 * All calculations use SI-compatible engineering units.
 */
public final class ElectricalProtectionCalculations {

    private static final double OVERLOAD_FACTOR = 1.45;

    private ElectricalProtectionCalculations() {
    }

    /**
     * Checks coordination between load, protective device
     * and cable current-carrying capacity.
     *
     * Ib <= In <= Iz
     *
     * Ib - design load current.
     * In - rated protective device current.
     * Iz - cable current-carrying capacity.
     */
    public static boolean isCableProtectionCoordinated(
            double loadCurrent,
            double ratedProtectionCurrent,
            double cableCurrentCapacity
    ) {
        validateNonNegativeFinite(loadCurrent, "Load current");
        validatePositiveFinite(
                ratedProtectionCurrent,
                "Rated protection current"
        );
        validatePositiveFinite(
                cableCurrentCapacity,
                "Cable current capacity"
        );

        return loadCurrent <= ratedProtectionCurrent
                && ratedProtectionCurrent <= cableCurrentCapacity;
    }

    /**
     * Checks the conventional overload protection condition.
     *
     * I2 <= 1.45 * Iz
     *
     * I2 - conventional operating current of the protective device.
     * Iz - cable current-carrying capacity.
     */
    public static boolean isOverloadProtectionCompliant(
            double conventionalOperatingCurrent,
            double cableCurrentCapacity
    ) {
        validatePositiveFinite(
                conventionalOperatingCurrent,
                "Conventional operating current"
        );
        validatePositiveFinite(
                cableCurrentCapacity,
                "Cable current capacity"
        );

        return conventionalOperatingCurrent
                <= OVERLOAD_FACTOR * cableCurrentCapacity;
    }

    /**
     * Calculates the overload ratio.
     *
     * k = I / In
     *
     * I - actual current.
     * In - rated protection current.
     */
    public static double calculateOverloadRatio(
            double actualCurrent,
            double ratedProtectionCurrent
    ) {
        validateNonNegativeFinite(actualCurrent, "Actual current");
        validatePositiveFinite(
                ratedProtectionCurrent,
                "Rated protection current"
        );

        double result = actualCurrent / ratedProtectionCurrent;

        validateFiniteResult(result);

        return result;
    }

    /**
     * Checks whether a protective device can interrupt
     * the expected short-circuit current.
     *
     * Ik <= Icu
     *
     * Ik - expected short-circuit current.
     * Icu - rated ultimate short-circuit breaking capacity.
     */
    public static boolean canInterruptShortCircuit(
            double shortCircuitCurrent,
            double breakingCapacity
    ) {
        validateNonNegativeFinite(
                shortCircuitCurrent,
                "Short-circuit current"
        );
        validatePositiveFinite(
                breakingCapacity,
                "Breaking capacity"
        );

        return shortCircuitCurrent <= breakingCapacity;
    }

    /**
     * Checks the cable adiabatic thermal withstand condition.
     *
     * I^2 * t <= k^2 * S^2
     *
     * I - fault current.
     * t - disconnection time.
     * k - conductor and insulation material coefficient.
     * S - conductor cross-section in mm^2.
     */
    public static boolean isCableThermallyProtected(
            double faultCurrent,
            double disconnectionTime,
            double materialCoefficient,
            double cableCrossSection
    ) {
        validatePositiveFinite(faultCurrent, "Fault current");
        validatePositiveFinite(
                disconnectionTime,
                "Disconnection time"
        );
        validatePositiveFinite(
                materialCoefficient,
                "Material coefficient"
        );
        validatePositiveFinite(
                cableCrossSection,
                "Cable cross-section"
        );

        double faultThermalEnergy =
                faultCurrent * faultCurrent * disconnectionTime;

        double cableThermalLimit =
                materialCoefficient * materialCoefficient
                        * cableCrossSection * cableCrossSection;

        validateFiniteResult(faultThermalEnergy);
        validateFiniteResult(cableThermalLimit);

        return faultThermalEnergy <= cableThermalLimit;
    }

    /**
     * Calculates the maximum permissible disconnection time
     * according to the adiabatic cable withstand condition.
     *
     * t_max = (k * S / I)^2
     *
     * This calculation assumes constant fault current
     * and an adiabatic conductor model.
     */
    public static double calculateMaximumDisconnectionTime(
            double faultCurrent,
            double materialCoefficient,
            double cableCrossSection
    ) {
        validatePositiveFinite(faultCurrent, "Fault current");
        validatePositiveFinite(
                materialCoefficient,
                "Material coefficient"
        );
        validatePositiveFinite(
                cableCrossSection,
                "Cable cross-section"
        );

        double ratio =
                materialCoefficient * cableCrossSection
                        / faultCurrent;

        double result = ratio * ratio;

        validateFiniteResult(result);

        return result;
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