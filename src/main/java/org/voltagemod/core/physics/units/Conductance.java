package org.voltagemod.core.physics.units;

public final class Conductance extends PhysicalQuantity {

    public Conductance(double siemens) {
        super(siemens);
    }

    @Override
    public String getUnitSymbol() {
        return "S";
    }
}
