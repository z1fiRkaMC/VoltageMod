package org.voltagemod.core.physics.units;

public final class Current extends PhysicalQuantity {

    public Current(double amperes) {
        super(amperes);
    }

    @Override
    public String getUnitSymbol() {
        return "A";
    }
}
