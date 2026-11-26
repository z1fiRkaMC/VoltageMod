package org.voltagemod.core.simulation.ac;

import org.voltagemod.core.physics.electrical.Complex;

import java.util.*;

public final class AcNetwork {
    private final Map<String, AcNode> nodes = new LinkedHashMap<>();
    private final Map<String, AcElement> elements = new LinkedHashMap<>();
    private final List<AcVoltageSource> voltageSources = new ArrayList<>();
    private String groundNodeId;

    public AcNetwork addNode(AcNode node) {
        require(node, "node");
        if (nodes.containsKey(node.getId())) {
            throw new IllegalArgumentException("Duplicate node id: " + node.getId());
        }
        if (node.isGround()) {
            if (groundNodeId != null) throw new IllegalArgumentException("Only one ground node is allowed.");
            groundNodeId = node.getId();
        }
        nodes.put(node.getId(), node);
        return this;
    }

    public AcNetwork addElement(AcElement element) {
        require(element, "element");
        if (elements.containsKey(element.getId())) {
            throw new IllegalArgumentException("Duplicate element id: " + element.getId());
        }
        elements.put(element.getId(), element);
        if (element instanceof AcVoltageSource) {
            voltageSources.add((AcVoltageSource) element);
        }
        return this;
    }

    public AcSolution solve() {
        if (groundNodeId == null) {
            throw new IllegalStateException("Network must have a ground node.");
        }
        validateElementNodes();

        Map<String, Integer> nodeIndexes = new LinkedHashMap<>();
        int idx = 0;
        for (AcNode node : nodes.values()) {
            if (!node.isGround()) {
                nodeIndexes.put(node.getId(), Integer.valueOf(idx++));
            }
        }
        Map<String, Integer> sourceIndexes = new LinkedHashMap<>();
        for (int i = 0; i < voltageSources.size(); i++) {
            sourceIndexes.put(voltageSources.get(i).getId(), Integer.valueOf(i));
        }
        AcNetworkContext context = new AcNetworkContext(nodeIndexes, sourceIndexes);
        AcMnaMatrix matrix = new AcMnaMatrix(Math.max(1, context.getSize()));

        if (context.getSize() == 0) {
            AcSolutionBuilder empty = new AcSolutionBuilder();
            empty.putNodeVoltage(groundNodeId, Complex.ZERO);
            return empty.build();
        }

        for (AcElement element : elements.values()) {
            element.stamp(matrix, context);
        }

        Complex[] x = AcMnaSolver.solve(matrix.getA(), matrix.getZ());

        AcSolutionBuilder builder = new AcSolutionBuilder();
        builder.putNodeVoltage(groundNodeId, Complex.ZERO);
        for (Map.Entry<String, Integer> e : nodeIndexes.entrySet()) {
            builder.putNodeVoltage(e.getKey(), x[e.getValue().intValue()]);
        }
        for (AcVoltageSource source : voltageSources) {
            Complex current = x[context.getSourceIndex(source.getId())];
            source.fillResult(builder, current);
        }
        for (AcElement element : elements.values()) {
            if (element instanceof AcVoltageSource) continue;
            element.fillResult(builder, context);
        }
        return builder.build();
    }

    private void validateElementNodes() {
        for (AcElement element : elements.values()) {
            if (element instanceof AcImpedanceElement) {
                AcImpedanceElement e = (AcImpedanceElement) element;
                requireNode(e.getNodeA(), e.getId());
                requireNode(e.getNodeB(), e.getId());
            } else if (element instanceof AcVoltageSource) {
                AcVoltageSource s = (AcVoltageSource) element;
                requireNode(s.getPositiveNodeId(), s.getId());
                requireNode(s.getNegativeNodeId(), s.getId());
            }
        }
    }

    private void requireNode(String nodeId, String elementId) {
        if (!nodes.containsKey(nodeId)) {
            throw new IllegalStateException("Element " + elementId + " references unknown node " + nodeId);
        }
    }

    private static void require(Object value, String name) {
        if (value == null) throw new IllegalArgumentException(name + " cannot be null.");
    }
}
