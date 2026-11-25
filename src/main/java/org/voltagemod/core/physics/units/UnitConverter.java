package org.voltagemod.core.physics.units;

public final class UnitConverter {

    private UnitConverter() {
    }

    public static double millivoltsToVolts(double millivolts) {
        return millivolts * 1e-3;
    }

    public static double kilovoltsToVolts(double kilovolts) {
        return kilovolts * 1e3;
    }

    public static double voltsToMillivolts(double volts) {
        return volts * 1e3;
    }

    public static double voltsToKilovolts(double volts) {
        return volts * 1e-3;
    }

    public static double milliampsToAmps(double milliamps) {
        return milliamps * 1e-3;
    }

    public static double kiloampsToAmps(double kiloamps) {
        return kiloamps * 1e3;
    }

    public static double ampsToMilliamps(double amps) {
        return amps * 1e3;
    }

    public static double ampsToKiloamps(double amps) {
        return amps * 1e-3;
    }

    public static double milliohmsToOhms(double milliohms) {
        return milliohms * 1e-3;
    }

    public static double kiloohmsToOhms(double kiloohms) {
        return kiloohms * 1e3;
    }

    public static double megaohmsToOhms(double megaohms) {
        return megaohms * 1e6;
    }

    public static double ohmsToKiloohms(double ohms) {
        return ohms * 1e-3;
    }

    public static double ohmsToMegaohms(double ohms) {
        return ohms * 1e-6;
    }

    public static double milliwattsToWatts(double milliwatts) {
        return milliwatts * 1e-3;
    }

    public static double kilowattsToWatts(double kilowatts) {
        return kilowatts * 1e3;
    }

    public static double megawattsToWatts(double megawatts) {
        return megawatts * 1e6;
    }

    public static double wattsToKilowatts(double watts) {
        return watts * 1e-3;
    }

    public static double wattsToMegawatts(double watts) {
        return watts * 1e-6;
    }

    public static double wattHoursToJoules(double wattHours) {
        return wattHours * 3600.0;
    }

    public static double kilowattHoursToJoules(double kilowattHours) {
        return kilowattHours * 3.6e6;
    }

    public static double joulesToWattHours(double joules) {
        return joules / 3600.0;
    }

    public static double joulesToKilowattHours(double joules) {
        return joules / 3.6e6;
    }

    public static double kilohertzToHertz(double kilohertz) {
        return kilohertz * 1e3;
    }

    public static double megahertzToHertz(double megahertz) {
        return megahertz * 1e6;
    }

    public static double hertzToKilohertz(double hertz) {
        return hertz * 1e-3;
    }

    public static double microfaradsToFarads(double microfarads) {
        return microfarads * 1e-6;
    }

    public static double nanofaradsToFarads(double nanofarads) {
        return nanofarads * 1e-9;
    }

    public static double faradsToMicrofarads(double farads) {
        return farads * 1e6;
    }

    public static double millihenriesToHenries(double millihenries) {
        return millihenries * 1e-3;
    }

    public static double microhenriesToHenries(double microhenries) {
        return microhenries * 1e-6;
    }

    public static double henriesToMillihenries(double henries) {
        return henries * 1e3;
    }

    public static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }

    public static double celsiusToFahrenheit(double celsius) {
        return celsius * 9.0 / 5.0 + 32.0;
    }
}
