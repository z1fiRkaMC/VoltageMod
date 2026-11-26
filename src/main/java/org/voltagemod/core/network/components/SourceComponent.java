package org.voltagemod.core.network.components;

import org.voltagemod.core.network.ElectricalComponent;
import org.voltagemod.core.network.NetworkSolveContext;
import org.voltagemod.core.simulation.ac.AcNetwork;
import org.voltagemod.core.simulation.ac.AcVoltageSource;

import java.util.Arrays;
import java.util.List;

public final class SourceComponent implements ElectricalComponent {
    public static final String PHASE = "phase";
    public static final String NEUTRAL = "neutral";

    private final String id;
    private final double rmsVoltage;
    private final double phaseRadians;

    public SourceComponent(String id, double rmsVoltage, double phaseRadians) {
        this.id = id;
        this.rmsVoltage = rmsVoltage;
        this.phaseRadians = phaseRadians;
    }

    @Override public String getId() { return id; }
    @Override public List<String> getTerminalIds() { return Arrays.asList(PHASE, NEUTRAL); }

    @Override
    public void stamp(AcNetwork network, NetworkSolveContext context) {
        String p = context.getNodeId(id, PHASE);
        String n = context.getNodeId(id, NEUTRAL);
        network.addElement(new AcVoltageSource(id + "_src", p, n, rmsVoltage, phaseRadians));
    }
}
