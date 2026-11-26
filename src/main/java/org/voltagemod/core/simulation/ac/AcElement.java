package org.voltagemod.core.simulation.ac;

import org.voltagemod.core.physics.electrical.Complex;

public interface AcElement {
    String getId();

    void stamp(AcMnaMatrix matrix, AcNetworkContext context);

    void fillResult(AcSolutionBuilder solution, AcNetworkContext context);

    default Complex getCurrent(AcSolution solution) {
        return solution.getElementCurrent(getId());
    }
}
