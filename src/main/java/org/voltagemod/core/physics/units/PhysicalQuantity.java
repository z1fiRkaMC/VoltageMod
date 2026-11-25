package org.voltagemod.core.physics.units;

public abstract class PhysicalQuantity {

    private final double value;

    protected PhysicalQuantity(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(
                    "Physical quantity must be a finite number."
            );
        }

        this.value = value;
    }

    public final double getValue() {
        return value;
    }

    @Override
    public final String toString() {
        return getValue() + " " + getUnitSymbol();
    }

    public abstract String getUnitSymbol();
}
