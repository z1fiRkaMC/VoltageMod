package org.voltagemod.core.physics.units;

public final class Temperature extends PhysicalQuantity {

    public Temperature(double kelvin) {
        super(kelvin);

        if (kelvin < 0.0) {
            throw new IllegalArgumentException(
                    "Temperature cannot be below absolute zero."
            );
        }
    }

    @Override
    public String getUnitSymbol() {
        return "K";
    }
}
