package org.voltagemod.core.simulation.ac;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class AcNetworkContext {
    private final Map<String, Integer> nodeIndexes;
    private final Map<String, Integer> sourceIndexes;
    private final int nodeCount;
    private final int size;

    public AcNetworkContext(Map<String, Integer> nodeIndexes, Map<String, Integer> sourceIndexes) {
        this.nodeIndexes = Collections.unmodifiableMap(new HashMap<>(nodeIndexes));
        this.sourceIndexes = Collections.unmodifiableMap(new HashMap<>(sourceIndexes));
        this.nodeCount = this.nodeIndexes.size();
        this.size = nodeCount + this.sourceIndexes.size();
    }

    public int getNodeIndex(String nodeId) {
        Integer idx = nodeIndexes.get(nodeId);
        if (idx == null) {
            throw new IllegalArgumentException("Unknown node: " + nodeId);
        }
        return idx.intValue();
    }

    public boolean hasNode(String nodeId) {
        return nodeIndexes.containsKey(nodeId);
    }

    public int getSourceIndex(String sourceId) {
        Integer idx = sourceIndexes.get(sourceId);
        if (idx == null) {
            throw new IllegalArgumentException("Unknown source: " + sourceId);
        }
        return nodeCount + idx.intValue();
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getSize() {
        return size;
    }

    public Map<String, Integer> getNodeIndexes() {
        return nodeIndexes;
    }

    public Map<String, Integer> getSourceIndexes() {
        return sourceIndexes;
    }
}
