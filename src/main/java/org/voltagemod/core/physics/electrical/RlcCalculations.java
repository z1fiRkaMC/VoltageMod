package org.voltagemod.core.physics.electrical;

public final class RlcCalculations {

    private RlcCalculations() {
    }

    public static Impedance calculateSeriesImpedance(
            double resistance,
            double frequency,
            double inductance,
            double capacitance
    ) {
        if (!Double.isFinite(resistance) || resistance < 0.0) {
            throw new IllegalArgumentException(
                    "Resistance must be finite and non-negative."
            );
        }

        double inductiveReactance =
                ReactanceCalculations.calculateInductiveReactance(
                        frequency,
                        inductance
                );

        double capacitiveReactance =
                ReactanceCalculations.calculateCapacitiveReactance(
                        frequency,
                        capacitance
                );

        double totalReactance =
                inductiveReactance + capacitiveReactance;

        return new Impedance(resistance, totalReactance);
    }
}
