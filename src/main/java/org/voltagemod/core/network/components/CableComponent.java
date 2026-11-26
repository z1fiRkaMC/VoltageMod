package org.voltagemod.core.network.components;

import org.voltagemod.core.network.ElectricalComponent;
import org.voltagemod.core.network.NetworkSolveContext;
import org.voltagemod.core.physics.electrical.Complex;
import org.voltagemod.core.simulation.ac.AcImpedanceElement;
import org.voltagemod.core.simulation.ac.AcNetwork;

import java.util.Arrays;
import java.util.List;

public final class CableComponent implements ElectricalComponent {
    public static final String A = "a";
    public static final String B = "b";

    private final String id;
    private final Complex impedance;

    public CableComponent(String id, double resistance, double reactance) {
        this.id = id;
        this.impedance = new Complex(resistance, reactance);
    }

    @Override public String getId() { return id; }
    @Override public List<String> getTerminalIds() { return Arrays.asList(A, B); }

    @Override
    public void stamp(AcNetwork network, NetworkSolveContext context) {
        String a = context.getNodeId(id, A);
        String b = context.getNodeId(id, B);
        network.addElement(new AcImpedanceElement(id + "_z", a, b, impedance));
    }
}
