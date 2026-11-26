package org.voltagemod.core.simulation.ac;

import org.voltagemod.core.physics.electrical.Complex;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AcSolutionBuilder {
    private final Map<String, Complex> nodeVoltages = new LinkedHashMap<>();
    private final Map<String, Complex> elementCurrents = new LinkedHashMap<>();
    private final Map<String, Complex> elementPowers = new LinkedHashMap<>();

    public void putNodeVoltage(String nodeId, Complex voltage) {
        require(nodeId, "nodeId");
        require(voltage, "voltage");
        nodeVoltages.put(nodeId, voltage);
    }

    public void putElementCurrent(String elementId, Complex current) {
        require(elementId, "elementId");
        require(current, "current");
        elementCurrents.put(elementId, current);
    }

    public void putElementPower(String elementId, Complex power) {
        require(elementId, "elementId");
        require(power, "power");
        elementPowers.put(elementId, power);
    }

    public Complex getNodeVoltage(String nodeId) {
        Complex v = nodeVoltages.get(nodeId);
        if (v == null) throw new IllegalArgumentException("Unknown node in solution builder: " + nodeId);
        return v;
    }

    public AcSolution build() {
        return new AcSolution(nodeVoltages, elementCurrents, elementPowers);
    }

    private static void require(Object value, String name) {
        if (value == null) throw new IllegalArgumentException(name + " cannot be null.");
    }
}
