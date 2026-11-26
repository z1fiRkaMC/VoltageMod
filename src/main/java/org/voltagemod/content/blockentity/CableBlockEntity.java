package org.voltagemod.content.blockentity;

import org.voltagemod.core.network.ElectricalComponent;
import org.voltagemod.core.network.ElectricalNetwork;
import org.voltagemod.core.network.NetworkSolveContext;
import org.voltagemod.core.physics.electrical.Complex;
import org.voltagemod.core.simulation.ac.AcImpedanceElement;
import org.voltagemod.core.simulation.ac.AcNetwork;
import org.voltagemod.core.simulation.ac.AcSolution;
import org.voltagemod.registry.ModBlockEntities;

import java.util.Arrays;
import java.util.List;

public class CableBlockEntity extends ElectricalBlockEntity {
    private double resistance = 0.5;
    private double reactance = 0.1;
    private double currentMagnitude;

    public CableBlockEntity() { super(ModBlockEntities.CABLE.get()); }
    @Override public String getComponentId() { return "cable_" + getBlockPos().asLong(); }
    @Override public List<String> getTerminalIds() {
        return Arrays.asList("phase_a", "phase_b", "neutral_a", "neutral_b");
    }

    @Override
    public void addToNetwork(ElectricalNetwork network) {
        final String id = getComponentId();
        network.addComponent(new ElectricalComponent() {
            @Override public String getId() { return id; }
            @Override public List<String> getTerminalIds() { return CableBlockEntity.this.getTerminalIds(); }
            @Override
            public void stamp(AcNetwork ac, NetworkSolveContext context) {
                String pa = context.getNodeId(id, "phase_a");
                String pb = context.getNodeId(id, "phase_b");
                String na = context.getNodeId(id, "neutral_a");
                String nb = context.getNodeId(id, "neutral_b");
                if (!pa.equals(pb)) {
                    ac.addElement(new AcImpedanceElement(id + "_phase", pa, pb, new Complex(resistance, reactance)));
                }
                if (!na.equals(nb)) {
                    ac.addElement(new AcImpedanceElement(id + "_neutral", na, nb, new Complex(0.001, 0.0)));
                }
            }
        });
    }

    @Override
    public void applySolution(AcSolution solution) {
        try { currentMagnitude = solution.getElementCurrent(getComponentId() + "_phase").magnitude(); }
        catch (Exception ignored) { currentMagnitude = 0; }
    }

    @Override public void clearSolution() { currentMagnitude = 0; }
}
