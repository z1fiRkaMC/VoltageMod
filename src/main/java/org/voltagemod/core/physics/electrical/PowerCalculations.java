package org.voltagemod.core.physics.electrical;

public final class PowerCalculations {

    private PowerCalculations() {
    }

    public static Complex calculateComplexPower(
        Complex voltage,
        Complex current
    ) {
        if (voltage == null || current == null) {
            throw new IllegalArgumentException(
                    "Voltage and current cannot be null."
            );
        }

        return voltage.multiply(current.conjugate());
    }

    public static double calculateActivePower(
            Complex voltage,
            Complex current
    ) {
        return calculateComplexPower(voltage, current).getReal();
    }

    public static double calculateReactivePower(
            Complex voltage,
            Complex current
    ) {
        return calculateComplexPower(voltage, current).getImaginary();
    }

    public static double calculateApparentPower(
            Complex voltage,
            Complex current
    ) {
        return calculateComplexPower(voltage, current).magnitude();
    }

    public static double calculatePowerFactor(
            Complex voltage,
            Complex current
    ) {
        Complex power = calculateComplexPower(voltage, current);
        double apparentPower = power.magnitude();

        if (apparentPower == 0.0) {
            throw new ArithmeticException(
                    "Power factor is undefined when apparent power is zero."
            );
        }

        return power.getReal() / apparentPower;
    }

    public static double calculatePowerAngle(
            Complex voltage,
            Complex current
    ) {
        Complex power = calculateComplexPower(voltage, current);

        return power.argument();
    }
}
