package org.voltagemod.core.network.components;

import org.voltagemod.core.network.ElectricalComponent;
import org.voltagemod.core.network.NetworkSolveContext;
import org.voltagemod.core.physics.electrical.Complex;
import org.voltagemod.core.simulation.ac.AcImpedanceElement;
import org.voltagemod.core.simulation.ac.AcNetwork;

import java.util.Arrays;
import java.util.List;

public final class LoadComponent implements ElectricalComponent {
    public static final String PHASE = "phase";
    public static final String NEUTRAL = "neutral";

    private final String id;
    private final Complex impedance;

    public LoadComponent(String id, double resistance, double reactance) {
        this.id = id;
        this.impedance = new Complex(resistance, reactance);
    }

    @Override public String getId() { return id; }
    @Override public List<String> getTerminalIds() { return Arrays.asList(PHASE, NEUTRAL); }

    @Override
    public void stamp(AcNetwork network, NetworkSolveContext context) {
        String p = context.getNodeId(id, PHASE);
        String n = context.getNodeId(id, NEUTRAL);
        network.addElement(new AcImpedanceElement(id + "_load", p, n, impedance));
    }
}
