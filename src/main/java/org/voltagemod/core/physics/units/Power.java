package org.voltagemod.core.physics.units;

public final class Power extends PhysicalQuantity {

    public Power(double watts) {
        super(watts);
    }

    @Override
    public String getUnitSymbol() {
        return "W";
    }
}
