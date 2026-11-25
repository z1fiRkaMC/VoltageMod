package org.voltagemod.core.physics.units;

public final class Frequency extends PhysicalQuantity {

    public Frequency(double hertz) {
        super(hertz);
    }

    @Override
    public String getUnitSymbol() {
        return "Hz";
    }
}
