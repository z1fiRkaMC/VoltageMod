package org.voltagemod.core.physics.units;

public final class Inductance extends PhysicalQuantity {

    public Inductance(double henries) {
        super(henries);
    }

    @Override
    public String getUnitSymbol() {
        return "H";
    }
}
