package org.voltagemod.core.physics.electrical;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VoltageDropCalculationsTest {

    private static final double EPS = 1e-9;

    @Test
    void calculatesDcVoltageDrop() {
        assertEquals(20.0, VoltageDropCalculations.calculateDcVoltageDrop(10.0, 2.0), EPS);
    }

    @Test
    void calculatesAcVoltageDrop() {
        double single = VoltageDropCalculations.calculateSinglePhaseAcVoltageDrop(10.0, 0.5, 0.2, 0.0);
        double three = VoltageDropCalculations.calculateThreePhaseAcVoltageDrop(10.0, 0.5, 0.2, 0.0);
        assertEquals(10.0, single, EPS); // 2*I*R
        assertEquals(Math.sqrt(3.0) * 10.0 * 0.5, three, EPS);
    }

    @Test
    void calculatesPercentageAndValidates() {
        assertEquals(5.0, VoltageDropCalculations.calculateVoltageDropPercentage(11.5, 230.0), EPS);
        assertThrows(ArithmeticException.class, () -> VoltageDropCalculations.calculateVoltageDropPercentage(1.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> VoltageDropCalculations.calculateDcVoltageDrop(-1.0, 1.0));
    }
}
