package org.voltagemod.core.physics.electrical;

import org.voltagemod.core.physics.units.Voltage;
import org.voltagemod.core.physics.units.Current;
import org.voltagemod.core.physics.units.Resistance;
import org.voltagemod.core.physics.units.Conductance;
import org.voltagemod.core.physics.units.Power;

public final class ElectricalCalculations {

    private ElectricalCalculations() {
    }

    public static Power calculatePower(Voltage voltage, Current current) {
        return new Power(
                voltage.getValue() * current.getValue()
        );
    }

    public static Voltage calculateVoltage(Current current, Resistance resistance) {
        return new Voltage(
                current.getValue() * resistance.getValue()
        );
    }

    public static Current calculateCurrent(Voltage voltage, Resistance resistance) {
        return new Current(
                voltage.getValue() / resistance.getValue()
        );
    }

    public static Resistance calculateResistance(Voltage voltage, Current current) {
        requireNonZero(current.getValue(), "Current");

        return new Resistance(
                voltage.getValue() / current.getValue()
        );
    }

    public static Conductance calculateConductance(Voltage voltage, Current current) {
        requireNonZero(voltage.getValue(), "Voltage");

        return new Conductance(
                current.getValue() / voltage.getValue()
        );
    }

    public static Conductance calculateConductance(Resistance resistance) {
        requireNonZero(resistance.getValue(), "Resistance");

        return new Conductance(
                1.0 / resistance.getValue()
        );
    }

    public static Resistance calculateResistance(Conductance conductance) {
        requireNonZero(conductance.getValue(), "Conductance");

        return new Resistance(
                1.0 / conductance.getValue()
        );
    }

    private static void requireNonZero(double value, String quantityName) {
        if (value == 0.0) {
            throw new ArithmeticException(
                    quantityName + " cannot be zero in this calculation."
            );
        }
    }
}
