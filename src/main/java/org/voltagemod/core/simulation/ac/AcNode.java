package org.voltagemod.core.simulation.ac;

public final class AcNode {
    private final String id;
    private final boolean ground;

    public AcNode(String id, boolean ground) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Node id cannot be null or empty.");
        }
        this.id = id;
        this.ground = ground;
    }

    public static AcNode ground(String id) {
        return new AcNode(id, true);
    }

    public static AcNode of(String id) {
        return new AcNode(id, false);
    }

    public String getId() {
        return id;
    }

    public boolean isGround() {
        return ground;
    }

    @Override
    public String toString() {
        return ground ? id + "(gnd)" : id;
    }
}
