package org.voltagemod.core.simulation.ac;

import org.voltagemod.core.physics.electrical.Complex;
import org.voltagemod.core.physics.electrical.PowerCalculations;

public final class AcImpedanceElement implements AcElement {
    private final String id;
    private final String nodeA;
    private final String nodeB;
    private final Complex impedance;

    public AcImpedanceElement(String id, String nodeA, String nodeB, Complex impedance) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("Element id cannot be null or empty.");
        if (nodeA == null || nodeB == null) throw new IllegalArgumentException("Element nodes cannot be null.");
        if (nodeA.equals(nodeB)) throw new IllegalArgumentException("Element nodes must be different.");
        if (impedance == null) throw new IllegalArgumentException("Impedance cannot be null.");
        if (impedance.getReal() == 0.0 && impedance.getImaginary() == 0.0) {
            throw new IllegalArgumentException("Impedance cannot be zero; use a voltage source/short model.");
        }
        this.id = id;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.impedance = impedance;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getNodeA() {
        return nodeA;
    }

    public String getNodeB() {
        return nodeB;
    }

    public Complex getImpedance() {
        return impedance;
    }

    private Complex admittance() {
        return Complex.ONE.divide(impedance);
    }

    @Override
    public void stamp(AcMnaMatrix matrix, AcNetworkContext context) {
        Complex y = admittance();
        if (context.hasNode(nodeA)) {
            int a = context.getNodeIndex(nodeA);
            matrix.add(a, a, y);
        }
        if (context.hasNode(nodeB)) {
            int b = context.getNodeIndex(nodeB);
            matrix.add(b, b, y);
        }
        if (context.hasNode(nodeA) && context.hasNode(nodeB)) {
            int a = context.getNodeIndex(nodeA);
            int b = context.getNodeIndex(nodeB);
            matrix.add(a, b, y.negate());
            matrix.add(b, a, y.negate());
        }
    }

    @Override
    public void fillResult(AcSolutionBuilder solution, AcNetworkContext context) {
        Complex va = nodeVoltage(solution, nodeA);
        Complex vb = nodeVoltage(solution, nodeB);
        Complex v = va.subtract(vb);
        Complex i = v.divide(impedance); // current from A to B
        solution.putElementCurrent(id, i);
        solution.putElementPower(id, PowerCalculations.calculateComplexPower(v, i));
    }

    private static Complex nodeVoltage(AcSolutionBuilder solution, String nodeId) {
        try {
            return solution.getNodeVoltage(nodeId);
        } catch (IllegalArgumentException e) {
            return Complex.ZERO; // ground not stored in node voltage map
        }
    }
}
