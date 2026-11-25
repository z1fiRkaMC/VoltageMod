package org.voltagemod.core.physics.electrical;

/**
 * Symmetrical components calculations using Fortescue transformation.
 *
 * Supports direct, inverse and zero sequence components.
 * All input values are complex phasors.
 */
public final class SymmetricalComponents {

    private static final double SQRT_THREE = Math.sqrt(3.0);

    private static final Complex A = new Complex(
            -0.5,
            SQRT_THREE / 2.0
    );

    private static final Complex A_SQUARED = new Complex(
            -0.5,
            -SQRT_THREE / 2.0
    );

    private static final double ONE_THIRD = 1.0 / 3.0;

    private SymmetricalComponents() {
    }

    /**
     * Calculates zero-sequence component.
     *
     * X0 = (XA + XB + XC) / 3
     */
    public static Complex calculateZeroSequence(
            Complex phaseA,
            Complex phaseB,
            Complex phaseC
    ) {
        validatePhases(phaseA, phaseB, phaseC);

        return phaseA
                .add(phaseB)
                .add(phaseC)
                .multiply(new Complex(ONE_THIRD, 0.0));
    }

    /**
     * Calculates positive-sequence component.
     *
     * X1 = (XA + a*XB + a^2*XC) / 3
     */
    public static Complex calculatePositiveSequence(
            Complex phaseA,
            Complex phaseB,
            Complex phaseC
    ) {
        validatePhases(phaseA, phaseB, phaseC);

        return phaseA
                .add(A.multiply(phaseB))
                .add(A_SQUARED.multiply(phaseC))
                .multiply(new Complex(ONE_THIRD, 0.0));
    }

    /**
     * Calculates negative-sequence component.
     *
     * X2 = (XA + a^2*XB + a*XC) / 3
     */
    public static Complex calculateNegativeSequence(
            Complex phaseA,
            Complex phaseB,
            Complex phaseC
    ) {
        validatePhases(phaseA, phaseB, phaseC);

        return phaseA
                .add(A_SQUARED.multiply(phaseB))
                .add(A.multiply(phaseC))
                .multiply(new Complex(ONE_THIRD, 0.0));
    }

    /**
     * Performs the complete Fortescue transformation.
     *
     * Result order:
     * [zero sequence, positive sequence, negative sequence]
     */
    public static Complex[] calculateAllSequences(
            Complex phaseA,
            Complex phaseB,
            Complex phaseC
    ) {
        validatePhases(phaseA, phaseB, phaseC);

        return new Complex[]{
                calculateZeroSequence(phaseA, phaseB, phaseC),
                calculatePositiveSequence(phaseA, phaseB, phaseC),
                calculateNegativeSequence(phaseA, phaseB, phaseC)
        };
    }

    /**
     * Reconstructs phase A from symmetrical components.
     *
     * XA = X0 + X1 + X2
     */
    public static Complex reconstructPhaseA(
            Complex zeroSequence,
            Complex positiveSequence,
            Complex negativeSequence
    ) {
        validateSequences(
                zeroSequence,
                positiveSequence,
                negativeSequence
        );

        return zeroSequence
                .add(positiveSequence)
                .add(negativeSequence);
    }

    /**
     * Reconstructs phase B from symmetrical components.
     *
     * XB = X0 + a^2*X1 + a*X2
     */
    public static Complex reconstructPhaseB(
            Complex zeroSequence,
            Complex positiveSequence,
            Complex negativeSequence
    ) {
        validateSequences(
                zeroSequence,
                positiveSequence,
                negativeSequence
        );

        return zeroSequence
                .add(A_SQUARED.multiply(positiveSequence))
                .add(A.multiply(negativeSequence));
    }

/**
 * Reconstructs phase C from symmetrical components.
 *
 * XC = X0 + a*X1 + a^2*X2
 */
public static Complex reconstructPhaseC(
        Complex zeroSequence,
        Complex positiveSequence,
        Complex negativeSequence
) {
    validateSequences(
            zeroSequence,
            positiveSequence,
            negativeSequence
    );

    return zeroSequence
            .add(A.multiply(positiveSequence))
            .add(A_SQUARED.multiply(negativeSequence));
}

    /**
     * Reconstructs all three phases.
     *
     * Result order: [phase A, phase B, phase C]
     */
    public static Complex[] reconstructPhases(
            Complex zeroSequence,
            Complex positiveSequence,
            Complex negativeSequence
    ) {
        validateSequences(
                zeroSequence,
                positiveSequence,
                negativeSequence
        );

        return new Complex[]{
                reconstructPhaseA(
                        zeroSequence,
                        positiveSequence,
                        negativeSequence
                ),
                reconstructPhaseB(
                        zeroSequence,
                        positiveSequence,
                        negativeSequence
                ),
                reconstructPhaseC(
                        zeroSequence,
                        positiveSequence,
                        negativeSequence
                )
        };
    }

    private static void validatePhases(
            Complex phaseA,
            Complex phaseB,
            Complex phaseC
    ) {
        if (phaseA == null || phaseB == null || phaseC == null) {
            throw new IllegalArgumentException(
                    "Phase values cannot be null."
            );
        }
    }

    private static void validateSequences(
            Complex zeroSequence,
            Complex positiveSequence,
            Complex negativeSequence
    ) {
        if (zeroSequence == null
                || positiveSequence == null
                || negativeSequence == null) {

            throw new IllegalArgumentException(
                    "Sequence components cannot be null."
            );
        }
    }
}