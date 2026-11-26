package org.voltagemod.core.network;

import org.voltagemod.core.simulation.ac.AcNetwork;
import org.voltagemod.core.simulation.ac.AcNode;
import org.voltagemod.core.simulation.ac.AcSolution;

import java.util.*;

public final class ElectricalNetwork {
    private final Map<String, ElectricalComponent> components = new LinkedHashMap<>();
    private final Set<Connection> connections = new LinkedHashSet<>();

    public void addComponent(ElectricalComponent component) {
        if (component == null) throw new IllegalArgumentException("component cannot be null");
        if (components.containsKey(component.getId())) throw new IllegalArgumentException("Duplicate component: " + component.getId());
        components.put(component.getId(), component);
    }

    public void connect(ElectricalTerminal a, ElectricalTerminal b) {
        if (a == null || b == null) throw new IllegalArgumentException("terminals cannot be null");
        connections.add(new Connection(a, b));
    }

    public AcSolution solve() {
        if (components.isEmpty()) throw new IllegalStateException("Network has no components");
        String groundNodeId = "gnd";
        Map<ElectricalTerminal, String> terminalNodes = assignNodes(groundNodeId);
        NetworkSolveContext ctx = new NetworkSolveContext(terminalNodes, groundNodeId);

        AcNetwork ac = new AcNetwork();
        ac.addNode(AcNode.ground(groundNodeId));
        Set<String> created = new HashSet<>();
        created.add(groundNodeId);
        for (String nodeId : terminalNodes.values()) {
            if (created.add(nodeId)) ac.addNode(AcNode.of(nodeId));
        }
        // allow components to create internal nodes by pre-stamping? Simpler: components stamp and we create nodes lazily not supported.
        // We do a two-pass: first pass stamps into a temp network wrapper not possible. Instead components must only use ctx.getNodeId.
        for (ElectricalComponent component : components.values()) {
            component.stamp(ac, ctx);
        }
        return ac.solve();
    }

    private Map<ElectricalTerminal, String> assignNodes(String groundNodeId) {
        Map<ElectricalTerminal, ElectricalTerminal> parent = new HashMap<>();
        for (ElectricalComponent c : components.values()) {
            for (String t : c.getTerminalIds()) {
                ElectricalTerminal et = new ElectricalTerminal(c.getId(), t);
                parent.put(et, et);
            }
        }
        for (Connection conn : connections) {
            union(parent, conn.a, conn.b);
        }
        Map<ElectricalTerminal, String> rootsToNode = new HashMap<>();
        Map<ElectricalTerminal, String> result = new HashMap<>();
        int idx = 1;
        for (ElectricalTerminal t : parent.keySet()) {
            ElectricalTerminal root = find(parent, t);
            String node = rootsToNode.get(root);
            if (node == null) {
                node = isNeutral(root) ? groundNodeId : "n" + (idx++);
                rootsToNode.put(root, node);
            }
            result.put(t, node);
        }
        // Ensure any neutral-named terminal maps to ground for MVP.
        for (Map.Entry<ElectricalTerminal, String> e : result.entrySet()) {
            if (isNeutral(e.getKey())) e.setValue(groundNodeId);
        }
        return result;
    }

    private static boolean isNeutral(ElectricalTerminal t) {
        String id = t.getTerminalId();
        return "neutral".equalsIgnoreCase(id) || "n".equalsIgnoreCase(id) || "gnd".equalsIgnoreCase(id) || "ground".equalsIgnoreCase(id);
    }

    private static ElectricalTerminal find(Map<ElectricalTerminal, ElectricalTerminal> parent, ElectricalTerminal x) {
        ElectricalTerminal p = parent.get(x);
        if (p == null) throw new IllegalArgumentException("Unknown terminal: " + x);
        if (p.equals(x)) return x;
        ElectricalTerminal root = find(parent, p);
        parent.put(x, root);
        return root;
    }

    private static void union(Map<ElectricalTerminal, ElectricalTerminal> parent, ElectricalTerminal a, ElectricalTerminal b) {
        if (!parent.containsKey(a) || !parent.containsKey(b)) throw new IllegalArgumentException("Connection references unknown terminal");
        ElectricalTerminal ra = find(parent, a);
        ElectricalTerminal rb = find(parent, b);
        if (ra.equals(rb)) return;
        // prefer neutral/ground root
        if (isNeutral(ra)) parent.put(rb, ra);
        else parent.put(ra, rb);
    }

    private static final class Connection {
        final ElectricalTerminal a;
        final ElectricalTerminal b;
        Connection(ElectricalTerminal a, ElectricalTerminal b) { this.a = a; this.b = b; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Connection)) return false;
            Connection that = (Connection) o;
            return (a.equals(that.a) && b.equals(that.b)) || (a.equals(that.b) && b.equals(that.a));
        }
        @Override public int hashCode() { return a.hashCode() + b.hashCode(); }
    }
}
