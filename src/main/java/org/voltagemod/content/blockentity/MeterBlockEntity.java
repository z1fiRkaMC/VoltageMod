package org.voltagemod.content.blockentity;

import org.voltagemod.core.network.ElectricalNetwork;
import org.voltagemod.core.network.components.LoadComponent;
import org.voltagemod.core.simulation.ac.AcSolution;
import org.voltagemod.registry.ModBlockEntities;

import java.util.Arrays;
import java.util.List;

public class MeterBlockEntity extends ElectricalBlockEntity {
    private double voltage;
    private double current;
    private double power;
    private double powerFactor;

    public MeterBlockEntity() { super(ModBlockEntities.METER.get()); }
    @Override public String getComponentId() { return "meter_" + getBlockPos().asLong(); }
    @Override public List<String> getTerminalIds() { return Arrays.asList(LoadComponent.PHASE, LoadComponent.NEUTRAL); }
    @Override public void addToNetwork(ElectricalNetwork network) {
        // High impedance probe to avoid altering circuit significantly
        network.addComponent(new LoadComponent(getComponentId(), 1_000_000.0, 0.0));
    }
    @Override public void applySolution(AcSolution solution) {
        try {
            org.voltagemod.core.physics.electrical.Complex i = solution.getElementCurrent(getComponentId() + "_load");
            org.voltagemod.core.physics.electrical.Complex s = solution.getElementPower(getComponentId() + "_load");
            current = i.magnitude();
            double apparent = s.magnitude();
            power = s.getReal();
            voltage = current > 0 ? apparent / current : 0;
            powerFactor = apparent > 0 ? power / apparent : 0;
        } catch (Exception ignored) { clearSolution(); return; }
        syncToClient();
    }
    @Override public void clearSolution() { voltage = current = power = powerFactor = 0; syncToClient(); }

    @Override
    public net.minecraft.nbt.CompoundNBT save(net.minecraft.nbt.CompoundNBT tag) {
        tag.putDouble("voltage", voltage);
        tag.putDouble("current", current);
        tag.putDouble("power", power);
        tag.putDouble("powerFactor", powerFactor);
        return super.save(tag);
    }

    @Override
    public void load(net.minecraft.block.BlockState state, net.minecraft.nbt.CompoundNBT tag) {
        super.load(state, tag);
        voltage = tag.getDouble("voltage");
        current = tag.getDouble("current");
        power = tag.getDouble("power");
        powerFactor = tag.getDouble("powerFactor");
    }

    public double getVoltage() { return voltage; }
    public double getCurrent() { return current; }
    public double getPower() { return power; }
    public double getPowerFactor() { return powerFactor; }
}
