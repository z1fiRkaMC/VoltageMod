package org.voltagemod.content.blockentity;

import org.voltagemod.core.network.ElectricalNetwork;
import org.voltagemod.core.network.components.CableComponent;
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
    @Override public List<String> getTerminalIds() { return Arrays.asList(CableComponent.A, CableComponent.B); }
    @Override public void addToNetwork(ElectricalNetwork network) { network.addComponent(new CableComponent(getComponentId(), resistance, reactance)); }
    @Override public void applySolution(AcSolution solution) {
        try { currentMagnitude = solution.getElementCurrent(getComponentId() + "_z").magnitude(); } catch (Exception ignored) { currentMagnitude = 0; }
    }
    @Override public void clearSolution() { currentMagnitude = 0; }
}
