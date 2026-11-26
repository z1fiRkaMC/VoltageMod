package org.voltagemod.core.physics.electrical;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortCircuitCalculationsTest {

    private static final double EPS = 1e-9;

    @Test
    void calculatesSinglePhaseShortCircuit() {
        Complex v = new Complex(230.0, 0.0);
        Complex zLoop = new Complex(0.2, 0.1);
        Complex zFault = new Complex(0.0, 0.0);

        Complex i = ShortCircuitCalculations.calculateSinglePhaseShortCircuitCurrent(v, zLoop, zFault);
        Complex expected = v.divide(zLoop);
        assertEquals(expected.getReal(), i.getReal(), EPS);
        assertEquals(expected.getImaginary(), i.getImaginary(), EPS);
        assertEquals(expected.magnitude(), ShortCircuitCalculations.calculateSinglePhaseShortCircuitCurrentMagnitude(v, zLoop, zFault), EPS);
    }

    @Test
    void calculatesThreePhaseShortCircuit() {
        double lineVoltage = 400.0;
        Complex z1 = new Complex(0.05, 0.05);
        Complex i = ShortCircuitCalculations.calculateThreePhaseShortCircuitCurrent(lineVoltage, z1, Complex.ZERO);
        double expectedMagnitude = (lineVoltage / Math.sqrt(3.0)) / z1.magnitude();
        assertEquals(expectedMagnitude, i.magnitude(), 1e-9);
        assertEquals(Math.sqrt(3.0) * lineVoltage * expectedMagnitude,
                ShortCircuitCalculations.calculateThreePhaseShortCircuitPower(lineVoltage, expectedMagnitude), 1e-6);
    }

    @Test
    void rejectsInvalidShortCircuitInputs() {
        assertThrows(ArithmeticException.class, () ->
                ShortCircuitCalculations.calculateSinglePhaseShortCircuitCurrent(new Complex(230, 0), Complex.ZERO, Complex.ZERO));
        assertThrows(IllegalArgumentException.class, () ->
                ShortCircuitCalculations.calculateThreePhaseShortCircuitPower(-1.0, 10.0));
    }
}
