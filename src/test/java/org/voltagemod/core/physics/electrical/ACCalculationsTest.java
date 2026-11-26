package org.voltagemod.core.physics.electrical;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ACCalculationsTest {

    private static final double EPS = 1e-9;

    @Test
    void calculatesRmsValues() {
        assertEquals(230.0 / Math.sqrt(2.0), ACCalculations.calculateRmsVoltage(230.0), EPS);
        assertEquals(10.0 / Math.sqrt(2.0), ACCalculations.calculateRmsCurrent(10.0), EPS);
    }

    @Test
    void calculatesActiveReactiveAndApparentPower() {
        double voltage = 230.0;
        double current = 10.0;
        double phaseAngle = Math.PI / 6.0; // 30 degrees, PF ~0.866

        assertEquals(2300.0, ACCalculations.calculateApparentPower(voltage, current), EPS);
        assertEquals(2300.0 * Math.cos(phaseAngle), ACCalculations.calculateActivePower(voltage, current, Math.cos(phaseAngle)), EPS);
        assertEquals(2300.0 * Math.sin(phaseAngle), ACCalculations.calculateReactivePower(voltage, current, phaseAngle), EPS);
    }

    @Test
    void calculatesPowerFactor() {
        assertEquals(0.8, ACCalculations.calculatePowerFactor(800.0, 1000.0), EPS);
    }

    @Test
    void rejectsInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> ACCalculations.calculateRmsVoltage(-1.0));
        assertThrows(IllegalArgumentException.class, () -> ACCalculations.calculateActivePower(230.0, 10.0, 1.2));
        assertThrows(ArithmeticException.class, () -> ACCalculations.calculatePowerFactor(100.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> ACCalculations.calculatePowerFactor(1200.0, 1000.0));
    }
}
