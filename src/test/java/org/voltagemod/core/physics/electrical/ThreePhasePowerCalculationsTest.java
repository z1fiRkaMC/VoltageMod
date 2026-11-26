package org.voltagemod.core.physics.electrical;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreePhasePowerCalculationsTest {

    private static final double EPS = 1e-9;

    @Test
    void calculatesBalancedThreePhasePower() {
        double vPhase = 230.0;
        double current = 10.0;
        double phaseAngle = -Math.PI / 6.0; // current lags by 30 deg

        Complex[] voltages = ThreePhaseCalculations.createBalancedPhaseVoltages(vPhase, PhaseAngle.fromDegrees(0.0));
        Complex[] currents = new Complex[] {
                Complex.fromPolar(current, -Math.PI / 6.0),
                Complex.fromPolar(current, -Math.PI / 6.0 - 2.0 * Math.PI / 3.0),
                Complex.fromPolar(current, -Math.PI / 6.0 + 2.0 * Math.PI / 3.0)
        };

        Complex total = ThreePhasePowerCalculations.calculateTotalComplexPower(voltages, currents);
        double expectedP = 3.0 * vPhase * current * Math.cos(Math.PI / 6.0);
        double expectedQ = 3.0 * vPhase * current * Math.sin(Math.PI / 6.0);

        assertEquals(expectedP, total.getReal(), 1e-6);
        assertEquals(expectedQ, total.getImaginary(), 1e-6);
        assertEquals(expectedP, ThreePhasePowerCalculations.calculateTotalActivePower(voltages, currents), 1e-6);
        assertEquals(expectedQ, ThreePhasePowerCalculations.calculateTotalReactivePower(voltages, currents), 1e-6);
        assertEquals(Math.sqrt(expectedP * expectedP + expectedQ * expectedQ),
                ThreePhasePowerCalculations.calculateTotalApparentPower(voltages, currents), 1e-6);
        assertEquals(Math.cos(Math.PI / 6.0),
                ThreePhasePowerCalculations.calculateTotalPowerFactor(voltages, currents), 1e-9);
    }

    @Test
    void rejectsInvalidPhaseArrays() {
        Complex[] voltages = new Complex[] { Complex.ONE, Complex.ONE };
        Complex[] currents = new Complex[] { Complex.ONE, Complex.ONE };
        assertThrows(IllegalArgumentException.class,
                () -> ThreePhasePowerCalculations.calculateTotalComplexPower(voltages, currents));
    }
}
