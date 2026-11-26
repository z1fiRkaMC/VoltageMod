package org.voltagemod.content.blockentity;

import org.voltagemod.core.network.ElectricalNetwork;
import org.voltagemod.core.network.components.SourceComponent;
import org.voltagemod.core.simulation.ac.AcSolution;
import org.voltagemod.registry.ModBlockEntities;

import java.util.Arrays;
import java.util.List;

public class GeneratorBlockEntity extends ElectricalBlockEntity {
    private double rmsVoltage = 230.0;
    private double phaseRadians = 0.0;

    public GeneratorBlockEntity() { super(ModBlockEntities.GENERATOR.get()); }

    @Override public String getComponentId() { return "gen_" + getBlockPos().asLong(); }
    @Override public List<String> getTerminalIds() { return Arrays.asList(SourceComponent.PHASE, SourceComponent.NEUTRAL); }

    @Override
    public void addToNetwork(ElectricalNetwork network) {
        network.addComponent(new SourceComponent(getComponentId(), rmsVoltage, phaseRadians));
    }

    @Override public void applySolution(AcSolution solution) { }
}
