package org.voltagemod.core.physics.units;

public final class Energy extends PhysicalQuantity {

    public Energy(double joules) {
        super(joules);
    }

    @Override
    public String getUnitSymbol() {
        return "J";
    }
}
