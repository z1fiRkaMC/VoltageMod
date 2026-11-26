package org.voltagemod.core.network;

import java.util.Objects;

public final class ElectricalTerminal {
    private final String componentId;
    private final String terminalId;

    public ElectricalTerminal(String componentId, String terminalId) {
        if (componentId == null || componentId.trim().isEmpty()) throw new IllegalArgumentException("componentId cannot be null or empty");
        if (terminalId == null || terminalId.trim().isEmpty()) throw new IllegalArgumentException("terminalId cannot be null or empty");
        this.componentId = componentId;
        this.terminalId = terminalId;
    }

    public String getComponentId() { return componentId; }
    public String getTerminalId() { return terminalId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElectricalTerminal)) return false;
        ElectricalTerminal that = (ElectricalTerminal) o;
        return componentId.equals(that.componentId) && terminalId.equals(that.terminalId);
    }

    @Override
    public int hashCode() { return Objects.hash(componentId, terminalId); }

    @Override
    public String toString() { return componentId + ":" + terminalId; }
}
