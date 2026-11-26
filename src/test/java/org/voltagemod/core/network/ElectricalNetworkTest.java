package org.voltagemod.core.network;

import org.junit.jupiter.api.Test;
import org.voltagemod.core.network.components.CableComponent;
import org.voltagemod.core.network.components.LoadComponent;
import org.voltagemod.core.network.components.SourceComponent;
import org.voltagemod.core.physics.electrical.Complex;
import org.voltagemod.core.simulation.ac.AcSolution;

import static org.junit.jupiter.api.Assertions.*;

class ElectricalNetworkTest {

    private static final double EPS = 1e-6;

    @Test
    void solvesSimpleSourceCableLoad() {
        ElectricalNetwork net = new ElectricalNetwork();
        SourceComponent src = new SourceComponent("src", 230.0, 0.0);
        CableComponent cable = new CableComponent("c1", 1.0, 0.5);
        LoadComponent load = new LoadComponent("load", 10.0, 0.0);
        net.addComponent(src);
        net.addComponent(cable);
        net.addComponent(load);

        net.connect(new ElectricalTerminal("src", SourceComponent.PHASE), new ElectricalTerminal("c1", CableComponent.A));
        net.connect(new ElectricalTerminal("c1", CableComponent.B), new ElectricalTerminal("load", LoadComponent.PHASE));
        net.connect(new ElectricalTerminal("src", SourceComponent.NEUTRAL), new ElectricalTerminal("load", LoadComponent.NEUTRAL));

        AcSolution s = net.solve();
        Complex i = s.getElementCurrent("c1_z");
        Complex expectedI = new Complex(230.0, 0.0).divide(new Complex(11.0, 0.5));
        assertEquals(expectedI.getReal(), i.getReal(), EPS);
        assertEquals(expectedI.getImaginary(), i.getImaginary(), EPS);
        Complex loadV = s.getElementPower("load_load"); // S = V*conj(I); not directly voltage, but current through load is same
        Complex loadI = s.getElementCurrent("load_load");
        assertEquals(expectedI.magnitude(), loadI.magnitude(), EPS);
        assertNotNull(loadV);
    }

    @Test
    void requiresGroundReference() {
        ElectricalNetwork net = new ElectricalNetwork();
        net.addComponent(new CableComponent("c1", 1.0, 0.0));
        assertThrows(Exception.class, net::solve);
    }
}
