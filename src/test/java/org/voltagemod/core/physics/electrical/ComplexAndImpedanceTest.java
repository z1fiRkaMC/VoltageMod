package org.voltagemod.core.physics.electrical;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexAndImpedanceTest {

    private static final double EPS = 1e-9;

    @Test
    void complexArithmetic() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, -2.0);

        Complex sum = a.add(b);
        Complex product = a.multiply(b);
        Complex quotient = a.divide(b);

        assertEquals(4.0, sum.getReal(), EPS);
        assertEquals(2.0, sum.getImaginary(), EPS);
        assertEquals(11.0, product.getReal(), EPS);
        assertEquals(-2.0, product.getImaginary(), EPS);
        assertEquals(-1.0, quotient.getReal(), EPS);
        assertEquals(2.0, quotient.getImaginary(), EPS);
    }

    @Test
    void complexPolarAndMagnitude() {
        Complex c = Complex.fromPolar(2.0, Math.PI / 2.0);
        assertEquals(0.0, c.getReal(), 1e-9);
        assertEquals(2.0, c.getImaginary(), 1e-9);
        assertEquals(5.0, new Complex(3.0, 4.0).magnitude(), EPS);
    }

    @Test
    void impedanceExposesComplexValues() {
        Impedance z = new Impedance(4.0, 3.0);
        assertEquals(5.0, z.getMagnitude(), EPS);
        assertEquals(Math.atan2(3.0, 4.0), z.getPhaseRadians(), EPS);
        assertEquals(4.0, z.getResistance(), EPS);
        assertEquals(3.0, z.getReactance(), EPS);
    }

    @Test
    void rejectsInvalidComplexOperations() {
        assertThrows(ArithmeticException.class, () -> new Complex(1.0, 1.0).divide(Complex.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new Complex(Double.NaN, 0.0));
    }
}
