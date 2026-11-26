package org.voltagemod.core.network.manager;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.voltagemod.content.blockentity.ElectricalBlockEntity;
import org.voltagemod.core.network.ElectricalNetwork;
import org.voltagemod.core.network.ElectricalTerminal;
import org.voltagemod.core.simulation.ac.AcSolution;

import java.util.*;

public final class ElectricalNetworkManager {
    private static final Map<ServerWorld, ElectricalNetworkManager> MANAGERS = new WeakHashMap<>();

    private final ServerWorld world;
    private final Map<BlockPos, ElectricalBlockEntity> components = new HashMap<>();
    private boolean dirty = true;

    private ElectricalNetworkManager(ServerWorld world) {
        this.world = world;
    }

    public static ElectricalNetworkManager get(ServerWorld world) {
        return MANAGERS.computeIfAbsent(world, ElectricalNetworkManager::new);
    }

    public void register(ElectricalBlockEntity be) {
        components.put(be.getBlockPos().immutable(), be);
        dirty = true;
    }

    public void unregister(ElectricalBlockEntity be) {
        if (components.remove(be.getBlockPos()) != null) dirty = true;
    }

    public void markDirty() { dirty = true; }

    public void tick() {
        if (!dirty) return;
        dirty = false;
        rebuildAndSolve();
    }

    private void rebuildAndSolve() {
        components.entrySet().removeIf(e -> e.getValue().isRemoved() || e.getValue().getLevel() != world);
        if (components.isEmpty()) return;
        ElectricalNetwork network = new ElectricalNetwork();
        for (ElectricalBlockEntity be : components.values()) {
            be.addToNetwork(network);
        }
        // simple MVP: connect adjacent electrical blockentities on all 6 dirs, matching terminal names phase<->phase, neutral<->neutral
        for (ElectricalBlockEntity be : components.values()) {
            BlockPos pos = be.getBlockPos();
            for (net.minecraft.util.Direction dir : net.minecraft.util.Direction.values()) {
                ElectricalBlockEntity other = components.get(pos.relative(dir));
                if (other == null) continue;
                connectMatching(network, be, other);
            }
        }
        try {
            AcSolution solution = network.solve();
            for (ElectricalBlockEntity be : components.values()) {
                be.applySolution(solution);
            }
        } catch (Exception e) {
            for (ElectricalBlockEntity be : components.values()) {
                be.clearSolution();
            }
        }
    }

    private void connectMatching(ElectricalNetwork network, ElectricalBlockEntity a, ElectricalBlockEntity b) {
        for (String ta : a.getTerminalIds()) {
            for (String tb : b.getTerminalIds()) {
                if (ta.equals(tb)) {
                    network.connect(new ElectricalTerminal(a.getComponentId(), ta), new ElectricalTerminal(b.getComponentId(), tb));
                }
            }
        }
    }
}
