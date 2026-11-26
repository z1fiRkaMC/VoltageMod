package org.voltagemod.core.simulation.ac;

import org.voltagemod.core.physics.electrical.Complex;
import org.voltagemod.core.physics.electrical.PowerCalculations;

public final class AcVoltageSource implements AcElement {
    private final String id;
    private final String positiveNodeId;
    private final String negativeNodeId;
    private final Complex voltage;

    public AcVoltageSource(String id, String positiveNodeId, String negativeNodeId, double rmsVoltage, double phaseRadians) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("Source id cannot be null or empty.");
        if (positiveNodeId == null || negativeNodeId == null) throw new IllegalArgumentException("Source nodes cannot be null.");
        if (positiveNodeId.equals(negativeNodeId)) throw new IllegalArgumentException("Source nodes must be different.");
        if (!Double.isFinite(rmsVoltage) || rmsVoltage < 0.0) throw new IllegalArgumentException("RMS voltage must be finite and non-negative.");
        if (!Double.isFinite(phaseRadians)) throw new IllegalArgumentException("Phase must be finite.");
        this.id = id;
        this.positiveNodeId = positiveNodeId;
        this.negativeNodeId = negativeNodeId;
        this.voltage = Complex.fromPolar(rmsVoltage, phaseRadians);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getPositiveNodeId() {
        return positiveNodeId;
    }

    public String getNegativeNodeId() {
        return negativeNodeId;
    }

    public Complex getVoltage() {
        return voltage;
    }

    @Override
    public void stamp(AcMnaMatrix matrix, AcNetworkContext context) {
        int s = context.getSourceIndex(id);
        if (context.hasNode(positiveNodeId)) {
            int p = context.getNodeIndex(positiveNodeId);
            matrix.add(p, s, Complex.ONE);
            matrix.add(s, p, Complex.ONE);
        }
        if (context.hasNode(negativeNodeId)) {
            int n = context.getNodeIndex(negativeNodeId);
            matrix.add(n, s, new Complex(-1.0, 0.0));
            matrix.add(s, n, new Complex(-1.0, 0.0));
        }
        matrix.addRhs(s, voltage);
    }

    @Override
    public void fillResult(AcSolutionBuilder solution, AcNetworkContext context) {
        // current is set by solver from x[sourceIndex]
    }

    public void fillResult(AcSolutionBuilder solution, Complex current) {
        solution.putElementCurrent(id, current);
        // source delivers power: S = V * conj(I)
        solution.putElementPower(id, PowerCalculations.calculateComplexPower(voltage, current));
    }
}
