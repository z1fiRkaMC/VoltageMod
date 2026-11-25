package org.voltagemod.core.physics.electrical;

import org.voltagemod.core.physics.units.Current;
import org.voltagemod.core.physics.units.Resistance;
import org.voltagemod.core.physics.units.Voltage;

public final class ElectricalSafetyCalculations {

    private ElectricalSafetyCalculations() {
    }

    /**
     * Расчёт тока через тело человека.
     *
     * I = U / (Rbody + Rcontact1 + Rcontact2)
     */
    public static Current calculateBodyCurrent(
            Voltage touchVoltage,
            Resistance bodyResistance,
            Resistance firstContactResistance,
            Resistance secondContactResistance) {

        if (touchVoltage == null || bodyResistance == null
                || firstContactResistance == null
                || secondContactResistance == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        double voltage = touchVoltage.getValue();
        double body = bodyResistance.getValue();
        double contact1 = firstContactResistance.getValue();
        double contact2 = secondContactResistance.getValue();

        requireNonNegative(voltage, "Touch voltage");
        requirePositive(body, "Body resistance");
        requireNonNegative(contact1, "First contact resistance");
        requireNonNegative(contact2, "Second contact resistance");

        double totalResistance = body + contact1 + contact2;

        return new Current(voltage / totalResistance);
    }

    /**
     * Напряжение прикосновения.
     * Ut = |phi_surface - phi_equipment|
     */
    public static Voltage calculateTouchVoltage(
            double equipmentPotential,
            double surfacePotential) {

        requireFinite(equipmentPotential, "Equipment potential");
        requireFinite(surfacePotential, "Surface potential");

        return new Voltage(Math.abs(surfacePotential - equipmentPotential));
    }

    /**
     * Напряжение шага.
     * Us = |phi1 - phi2|
     */
    public static Voltage calculateStepVoltage(
            double firstGroundPotential,
            double secondGroundPotential) {

        requireFinite(firstGroundPotential, "First ground potential");
        requireFinite(secondGroundPotential, "Second ground potential");

        return new Voltage(Math.abs(
                firstGroundPotential - secondGroundPotential
        ));
    }

    /**
     * Расчёт тока замыкания на землю.
     * If = U0 / |Zs + Zf|
     */
    public static Current calculateEarthFaultCurrent(
            Voltage phaseVoltage,
            Impedance loopImpedance,
            Impedance faultImpedance) {

        if (phaseVoltage == null || loopImpedance == null
                || faultImpedance == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        double voltage = phaseVoltage.getValue();

        requirePositive(voltage, "Phase voltage");

        Complex totalImpedance = loopImpedance.getValue()
                .add(faultImpedance.getValue());

        double impedanceMagnitude = totalImpedance.magnitude();

        if (impedanceMagnitude == 0.0) {
            throw new ArithmeticException(
                    "Total fault loop impedance cannot be zero"
            );
        }

        return new Current(voltage / impedanceMagnitude);
    }

    /**
     * Сопротивление одиночного вертикального заземлителя.
     *
     * Re = rho / (2 * PI * L) * (ln(4L/d) - 1)
     */
    public static Resistance calculateGroundingResistance(
            double soilResistivity,
            double electrodeLength,
            double electrodeDiameter) {

        requirePositive(soilResistivity, "Soil resistivity");
        requirePositive(electrodeLength, "Electrode length");
        requirePositive(electrodeDiameter, "Electrode diameter");

        if (electrodeLength <= electrodeDiameter) {
            throw new IllegalArgumentException(
                    "Electrode length must be greater than its diameter"
            );
        }

        double logarithm = Math.log(
                4.0 * electrodeLength / electrodeDiameter
        );

        double resistance = (soilResistivity
                / (2.0 * Math.PI * electrodeLength))
                * (logarithm - 1.0);

        if (!Double.isFinite(resistance) || resistance <= 0.0) {
            throw new ArithmeticException(
                    "Invalid grounding resistance result"
            );
        }

        return new Resistance(resistance);
    }

    /**
     * Потенциал заземляющего устройства.
     *
     * Ue = Ig * Re
     */
    public static Voltage calculateGroundPotentialRise(
            Current groundingCurrent,
            Resistance groundingResistance) {

        if (groundingCurrent == null || groundingResistance == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        double current = groundingCurrent.getValue();
        double resistance = groundingResistance.getValue();

        requireNonNegative(current, "Grounding current");
        requireNonNegative(resistance, "Grounding resistance");

        return new Voltage(current * resistance);
    }

    /**
     * Проверка условия автоматического отключения
     * для системы заземления TN.
     *
     * Zs * Ia <= U0
     */
    public static boolean isTNDisconnectionSatisfied(
            double loopImpedance,
            double operatingCurrent,
            double nominalPhaseVoltage) {

        requireNonNegative(loopImpedance, "Loop impedance");
        requirePositive(operatingCurrent, "Operating current");
        requirePositive(nominalPhaseVoltage, "Nominal phase voltage");

        return loopImpedance * operatingCurrent
                <= nominalPhaseVoltage;
    }

    /**
     * Проверка условия для системы заземления TT.
     *
     * RA * Ia <= UL
     */
    public static boolean isTTDisconnectionSatisfied(
            double groundingResistance,
            double residualOperatingCurrent,
            double permissibleTouchVoltage) {

        requireNonNegative(groundingResistance, "Grounding resistance");
        requirePositive(
                residualOperatingCurrent,
                "Residual operating current"
        );
        requirePositive(
                permissibleTouchVoltage,
                "Permissible touch voltage"
        );

        return groundingResistance * residualOperatingCurrent
                <= permissibleTouchVoltage;
    }

    private static void requireFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    name + " must be finite"
            );
        }
    }

    private static void requireNonNegative(double value, String name) {
        requireFinite(value, name);

        if (value < 0.0) {
            throw new IllegalArgumentException(
                    name + " cannot be negative"
            );
        }
    }

    private static void requirePositive(double value, String name) {
        requireFinite(value, name);

        if (value <= 0.0) {
            throw new IllegalArgumentException(
                    name + " must be greater than zero"
            );
        }
    }
}