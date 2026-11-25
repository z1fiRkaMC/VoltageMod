package org.voltagemod.core.physics.units;

public final class Capacitance extends PhysicalQuantity {

    public Capacitance(double farads) {
        super(farads);
    }

    @Override
    public String getUnitSymbol() {
        return "F";
    }
}
