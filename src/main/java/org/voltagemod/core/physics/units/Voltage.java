package org.voltagemod.core.physics.units;

public final class Voltage extends PhysicalQuantity {

    public Voltage(double volts) {
        super(volts);
    }

    @Override
    public String getUnitSymbol() {
        return "V";
    }
}
