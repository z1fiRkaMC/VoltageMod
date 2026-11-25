package org.voltagemod.core.physics.units;

public final class Resistance extends PhysicalQuantity {

    public Resistance(double ohms) {
        super(ohms);
    }

    @Override
    public String getUnitSymbol() {
        return "\u03A9";
    }
}
