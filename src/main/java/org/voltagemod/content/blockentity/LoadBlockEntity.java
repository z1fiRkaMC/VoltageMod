package org.voltagemod.content.blockentity;

import org.voltagemod.core.network.ElectricalNetwork;
import org.voltagemod.core.network.components.LoadComponent;
import org.voltagemod.core.simulation.ac.AcSolution;
import org.voltagemod.registry.ModBlockEntities;

import java.util.Arrays;
import java.util.List;

public class LoadBlockEntity extends ElectricalBlockEntity {
    private double resistance = 10.0;
    private double reactance = 0.0;
    private double currentMagnitude;

    public LoadBlockEntity() { super(ModBlockEntities.LOAD.get()); }
    @Override public String getComponentId() { return "load_" + getBlockPos().asLong(); }
    @Override public List<String> getTerminalIds() { return Arrays.asList(LoadComponent.PHASE, LoadComponent.NEUTRAL); }
    @Override public void addToNetwork(ElectricalNetwork network) { network.addComponent(new LoadComponent(getComponentId(), resistance, reactance)); }
    @Override public void applySolution(AcSolution solution) {
        try { currentMagnitude = solution.getElementCurrent(getComponentId() + "_load").magnitude(); } catch (Exception ignored) { currentMagnitude = 0; }
    }
    @Override public void clearSolution() { currentMagnitude = 0; }
}
