package org.voltagemod.core.physics.electrical;

/**
 * Harmonic analysis calculations.
 *
 * All voltage and current harmonic values are RMS values.
 * THD is returned as a percentage.
 */
public final class HarmonicCalculations {

    private HarmonicCalculations() {
    }

    /**
     * Calculates the frequency of a harmonic.
     *
     * fn = n * f1
     *
     * The fundamental harmonic has order 1.
     */
    public static double calculateHarmonicFrequency(
            double fundamentalFrequency,
            int harmonicOrder
    ) {
        validatePositiveFinite(
                fundamentalFrequency,
                "Fundamental frequency"
        );

        if (harmonicOrder < 1) {
            throw new IllegalArgumentException(
                    "Harmonic order must be at least 1."
            );
        }

        double result = fundamentalFrequency * harmonicOrder;

        validateFiniteResult(result);

        return result;
    }

    /**
     * Calculates total RMS value of a harmonic spectrum.
     *
     * Xrms = sqrt(X1^2 + X2^2 + ... + Xn^2)
     *
     * Array index 0 represents the fundamental harmonic.
     */
    public static double calculateTotalRms(double[] harmonics) {
        validateHarmonics(harmonics);

        double sumOfSquares = 0.0;

        for (double harmonic : harmonics) {
            sumOfSquares += harmonic * harmonic;
        }

        double result = Math.sqrt(sumOfSquares);

        validateFiniteResult(result);

        return result;
    }

    /**
     * Calculates RMS value of all harmonics excluding
     * the fundamental harmonic.
     */
    public static double calculateDistortionRms(
            double[] harmonics
    ) {
        validateHarmonics(harmonics);

        double sumOfSquares = 0.0;

        for (int i = 1; i < harmonics.length; i++) {
            sumOfSquares += harmonics[i] * harmonics[i];
        }

        double result = Math.sqrt(sumOfSquares);

        validateFiniteResult(result);

        return result;
    }

    /**
     * Calculates total harmonic distortion.
     *
     * THD = sqrt(X2^2 + X3^2 + ... + Xn^2) / X1 * 100%
     *
     * Array index 0 represents the fundamental harmonic.
     */
    public static double calculateTHD(double[] harmonics) {
        validateHarmonics(harmonics);

        double fundamental = harmonics[0];

        if (fundamental <= 0.0) {
            throw new IllegalArgumentException(
                    "Fundamental harmonic must be greater than zero."
            );
        }

        double distortionRms =
                calculateDistortionRms(harmonics);

        double result =
                distortionRms / fundamental * 100.0;

        validateFiniteResult(result);

        return result;
    }

    /**
     * Calculates voltage THD.
     */
    public static double calculateVoltageTHD(
            double[] voltageHarmonics
    ) {
        return calculateTHD(voltageHarmonics);
    }

    /**
     * Calculates current THD.
     */
    public static double calculateCurrentTHD(
            double[] currentHarmonics
    ) {
        return calculateTHD(currentHarmonics);
    }

    /**
     * Calculates the total RMS voltage.
     */
    public static double calculateTotalRmsVoltage(
            double[] voltageHarmonics
    ) {
        return calculateTotalRms(voltageHarmonics);
    }

    /**
     * Calculates the total RMS current.
     */
    public static double calculateTotalRmsCurrent(
            double[] currentHarmonics
    ) {
        return calculateTotalRms(currentHarmonics);
    }

    private static void validateHarmonics(double[] harmonics) {
        if (harmonics == null || harmonics.length == 0) {
            throw new IllegalArgumentException(
                    "Harmonic spectrum cannot be null or empty."
            );
        }

        for (int i = 0; i < harmonics.length; i++) {
            double value = harmonics[i];

            if (Double.isNaN(value)
                    || Double.isInfinite(value)
                    || value < 0.0) {

                throw new IllegalArgumentException(
                        "Harmonic RMS value at index "
                                + i
                                + " must be finite and non-negative."
                );
            }
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

    private static void validateFiniteResult(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new ArithmeticException(
                    "Calculation result is not finite."
            );
        }
    }
}