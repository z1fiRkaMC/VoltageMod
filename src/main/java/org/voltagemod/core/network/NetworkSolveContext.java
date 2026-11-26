package org.voltagemod.core.network;

import org.voltagemod.core.simulation.ac.AcNode;

import java.util.HashMap;
import java.util.Map;

public final class NetworkSolveContext {
    private final Map<ElectricalTerminal, String> terminalNodes;
    private final String groundNodeId;
    private int nextNodeId = 1;

    public NetworkSolveContext(Map<ElectricalTerminal, String> terminalNodes, String groundNodeId) {
        this.terminalNodes = new HashMap<>(terminalNodes);
        this.groundNodeId = groundNodeId;
    }

    public String getGroundNodeId() { return groundNodeId; }

    public String getNodeId(String componentId, String terminalId) {
        String node = terminalNodes.get(new ElectricalTerminal(componentId, terminalId));
        if (node == null) throw new IllegalArgumentException("No node for terminal " + componentId + ":" + terminalId);
        return node;
    }

    public String createInternalNodeId(String prefix) {
        return prefix + "_n" + (nextNodeId++);
    }

    public AcNode groundNode() { return AcNode.ground(groundNodeId); }
}
