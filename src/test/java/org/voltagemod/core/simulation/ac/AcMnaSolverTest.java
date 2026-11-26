package org.voltagemod.core.simulation.ac;

import org.junit.jupiter.api.Test;
import org.voltagemod.core.physics.electrical.Complex;

import static org.junit.jupiter.api.Assertions.*;

class AcMnaSolverTest {

    private static final double EPS = 1e-9;

    @Test
    void solvesSimpleResistiveDivider() {
        AcNetwork net = new AcNetwork();
        net.addNode(AcNode.ground("gnd"));
        net.addNode(AcNode.of("n1"));
        net.addNode(AcNode.of("n2"));
        net.addElement(new AcVoltageSource("V1", "n1", "gnd", 10.0, 0.0));
        net.addElement(new AcImpedanceElement("R1", "n1", "n2", new Complex(100.0, 0.0)));
        net.addElement(new AcImpedanceElement("R2", "n2", "gnd", new Complex(100.0, 0.0)));

        AcSolution s = net.solve();
        assertEquals(10.0, s.getNodeVoltage("n1").getReal(), EPS);
        assertEquals(5.0, s.getNodeVoltage("n2").getReal(), EPS);
        assertEquals(0.05, s.getElementCurrent("R1").getReal(), EPS);
    }

    @Test
    void solvesRlLoadPhaseAndPower() {
        AcNetwork net = new AcNetwork();
        net.addNode(AcNode.ground("gnd"));
        net.addNode(AcNode.of("n1"));
        net.addElement(new AcVoltageSource("V1", "n1", "gnd", 230.0, 0.0));
        // Z = 3 + j4 = 5 ohm, |I|=46A, PF=0.6 lagging
        net.addElement(new AcImpedanceElement("Z1", "n1", "gnd", new Complex(3.0, 4.0)));

        AcSolution s = net.solve();
        Complex i = s.getElementCurrent("Z1");
        assertEquals(46.0, i.magnitude(), 1e-6);
        assertEquals(-Math.atan2(4.0, 3.0), i.argument(), 1e-9);
        Complex p = s.getElementPower("Z1");
        assertEquals(6348.0, p.getReal(), 1e-4); // P = I^2*R = 2116*3
        assertEquals(8464.0, p.getImaginary(), 1e-4); // Q = I^2*X
    }

    @Test
    void solvesSourceLineLoadVoltageDrop() {
        AcNetwork net = new AcNetwork();
        net.addNode(AcNode.ground("gnd"));
        net.addNode(AcNode.of("src"));
        net.addNode(AcNode.of("load"));
        net.addElement(new AcVoltageSource("V1", "src", "gnd", 230.0, 0.0));
        net.addElement(new AcImpedanceElement("LINE", "src", "load", new Complex(1.0, 0.5)));
        net.addElement(new AcImpedanceElement("LOAD", "load", "gnd", new Complex(10.0, 0.0)));

        AcSolution s = net.solve();
        Complex vLoad = s.getNodeVoltage("load");
        Complex i = s.getElementCurrent("LINE");
        assertEquals(i.getReal(), s.getElementCurrent("LOAD").getReal(), 1e-9);
        assertEquals(i.getImaginary(), s.getElementCurrent("LOAD").getImaginary(), 1e-9);
        // manual: I = 230/(11+j0.5), Vload=I*10
        Complex expectedI = new Complex(230.0, 0.0).divide(new Complex(11.0, 0.5));
        assertEquals(expectedI.getReal(), i.getReal(), 1e-9);
        assertEquals(expectedI.getImaginary(), i.getImaginary(), 1e-9);
        assertEquals(expectedI.multiply(new Complex(10.0, 0.0)).getReal(), vLoad.getReal(), 1e-9);
    }

    @Test
    void detectsSingularMatrix() {
        AcNetwork net = new AcNetwork();
        net.addNode(AcNode.ground("gnd"));
        net.addNode(AcNode.of("n1"));
        // floating node, no reference to ground through elements except none -> singular when solving with no sources/elements
        net.addElement(new AcVoltageSource("V1", "n1", "gnd", 10.0, 0.0));
        // valid network; singular case: disconnected node with impedance to nowhere not possible here. Create explicit singular.
        assertThrows(ArithmeticException.class, () -> AcMnaSolver.solve(new Complex[][]{{Complex.ZERO}}, new Complex[]{Complex.ONE}));
    }

    @Test
    void rejectsInvalidNetwork() {
        AcNetwork net = new AcNetwork();
        net.addNode(AcNode.of("n1"));
        assertThrows(IllegalStateException.class, net::solve);
    }
}
