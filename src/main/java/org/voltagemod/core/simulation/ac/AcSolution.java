package org.voltagemod.core.simulation.ac;

import org.voltagemod.core.physics.electrical.Complex;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AcSolution {
    private final Map<String, Complex> nodeVoltages;
    private final Map<String, Complex> elementCurrents;
    private final Map<String, Complex> elementPowers;

    public AcSolution(Map<String, Complex> nodeVoltages, Map<String, Complex> elementCurrents, Map<String, Complex> elementPowers) {
        this.nodeVoltages = Collections.unmodifiableMap(new LinkedHashMap<>(nodeVoltages));
        this.elementCurrents = Collections.unmodifiableMap(new LinkedHashMap<>(elementCurrents));
        this.elementPowers = Collections.unmodifiableMap(new LinkedHashMap<>(elementPowers));
    }

    public Complex getNodeVoltage(String nodeId) {
        Complex v = nodeVoltages.get(nodeId);
        if (v == null) {
            throw new IllegalArgumentException("Unknown node in solution: " + nodeId);
        }
        return v;
    }

    public Complex getElementCurrent(String elementId) {
        Complex i = elementCurrents.get(elementId);
        if (i == null) {
            throw new IllegalArgumentException("Unknown element current in solution: " + elementId);
        }
        return i;
    }

    public Complex getElementPower(String elementId) {
        Complex s = elementPowers.get(elementId);
        if (s == null) {
            throw new IllegalArgumentException("Unknown element power in solution: " + elementId);
        }
        return s;
    }

    public Map<String, Complex> getNodeVoltages() {
        return nodeVoltages;
    }

    public Map<String, Complex> getElementCurrents() {
        return elementCurrents;
    }

    public Map<String, Complex> getElementPowers() {
        return elementPowers;
    }
}
