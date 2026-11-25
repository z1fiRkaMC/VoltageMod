package org.voltagemod.core.physics.electrical;

public final class PhaseAngle {

    private final double radians;

    public PhaseAngle(double radians) {
        if (!Double.isFinite(radians)) {
            throw new IllegalArgumentException(
                    "Phase angle must be finite."
            );
        }

        this.radians = normalize(radians);
    }

    public static PhaseAngle fromDegrees(double degrees) {
        if (!Double.isFinite(degrees)) {
            throw new IllegalArgumentException(
                    "Phase angle must be finite."
            );
        }

        return new PhaseAngle(Math.toRadians(degrees));
    }

    public double getRadians() {
        return radians;
    }

    public double getDegrees() {
        return Math.toDegrees(radians);
    }

    public double getSine() {
        return Math.sin(radians);
    }

    public double getCosine() {
        return Math.cos(radians);
    }

    public double getTangent() {
        return Math.tan(radians);
    }

    public PhaseAngle add(PhaseAngle other) {
        requireNotNull(other);
        return new PhaseAngle(radians + other.radians);
    }

    public PhaseAngle subtract(PhaseAngle other) {
        requireNotNull(other);
        return new PhaseAngle(radians - other.radians);
    }

    public PhaseAngle negate() {
        return new PhaseAngle(-radians);
    }

    private static double normalize(double angle) {
        double fullRotation = 2.0 * Math.PI;

        angle %= fullRotation;

        if (angle <= -Math.PI) {
            angle += fullRotation;
        } else if (angle > Math.PI) {
            angle -= fullRotation;
        }

        return angle;
    }

    private static void requireNotNull(PhaseAngle angle) {
        if (angle == null) {
            throw new IllegalArgumentException(
                    "Phase angle cannot be null."
            );
        }
    }

    @Override
    public String toString() {
        return getDegrees() + "°";
    }
}