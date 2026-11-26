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
    private volatile String phaseNodeA;
    private volatile String neutralNodeA;

    @Override public String getComponentId() { return "meter_" + getBlockPos().asLong(); }
    @Override public List<String> getTerminalIds() { return Arrays.asList("phase_a", "phase_b", "neutral_a", "neutral_b"); }
    @Override public void addToNetwork(ElectricalNetwork network) {
        final String id = getComponentId();
        network.addComponent(new org.voltagemod.core.network.ElectricalComponent() {
            @Override public String getId() { return id; }
            @Override public List<String> getTerminalIds() { return MeterBlockEntity.this.getTerminalIds(); }
            @Override
            public void stamp(org.voltagemod.core.simulation.ac.AcNetwork ac, org.voltagemod.core.network.NetworkSolveContext context) {
                String pa = context.getNodeId(id, "phase_a");
                String pb = context.getNodeId(id, "phase_b");
                String na = context.getNodeId(id, "neutral_a");
                String nb = context.getNodeId(id, "neutral_b");
                phaseNodeA = pa;
                neutralNodeA = na;
                org.voltagemod.core.physics.electrical.Complex z = new org.voltagemod.core.physics.electrical.Complex(0.001, 0.0);
                if (!pa.equals(pb)) {
                    ac.addElement(new org.voltagemod.core.simulation.ac.AcImpedanceElement(id + "_phase", pa, pb, z));
                }
                if (!na.equals(nb)) {
                    ac.addElement(new org.voltagemod.core.simulation.ac.AcImpedanceElement(id + "_neutral", na, nb, z));
                }
            }
        });
    }
    @Override public void applySolution(AcSolution solution) {
        try {
            org.voltagemod.core.physics.electrical.Complex vPhase = phaseNodeA != null ? solution.getNodeVoltage(phaseNodeA) : org.voltagemod.core.physics.electrical.Complex.ZERO;
            org.voltagemod.core.physics.electrical.Complex vNeutral = neutralNodeA != null ? solution.getNodeVoltage(neutralNodeA) : org.voltagemod.core.physics.electrical.Complex.ZERO;
            org.voltagemod.core.physics.electrical.Complex v = vPhase.subtract(vNeutral);
            voltage = v.magnitude();

            org.voltagemod.core.physics.electrical.Complex i = org.voltagemod.core.physics.electrical.Complex.ZERO;
            try {
                i = solution.getElementCurrent(getComponentId() + "_phase");
            } catch (Exception ignored) {
                // open circuit / no phase segment
            }
            current = i.magnitude();
            org.voltagemod.core.physics.electrical.Complex s = new org.voltagemod.core.physics.electrical.Complex(v.getReal() * i.getReal() + v.getImaginary() * i.getImaginary(),
                    v.getImaginary() * i.getReal() - v.getReal() * i.getImaginary());
            double apparent = s.magnitude();
            power = s.getReal();
            powerFactor = apparent > 1e-9 ? Math.max(-1.0, Math.min(1.0, power / apparent)) : 0.0;
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
