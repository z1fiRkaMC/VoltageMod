package org.voltagemod.core.network;

import org.voltagemod.core.simulation.ac.AcNetwork;

import java.util.List;

public interface ElectricalComponent {
    String getId();
    List<String> getTerminalIds();
    void stamp(AcNetwork network, NetworkSolveContext context);
}
