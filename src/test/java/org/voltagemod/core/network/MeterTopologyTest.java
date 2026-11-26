package org.voltagemod.core.network;

import org.junit.jupiter.api.Test;
import org.voltagemod.core.network.components.LoadComponent;
import org.voltagemod.core.network.components.SourceComponent;
import org.voltagemod.core.simulation.ac.AcImpedanceElement;
import org.voltagemod.core.simulation.ac.AcNetwork;
import org.voltagemod.core.simulation.ac.AcSolution;
import org.voltagemod.core.physics.electrical.Complex;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeterTopologyTest {
    static class PassThrough implements ElectricalComponent {
        final String id; final String a; final String b;
        PassThrough(String id, String a, String b){this.id=id;this.a=a;this.b=b;}
        public String getId(){return id;}
        public List<String> getTerminalIds(){return Arrays.asList(a,b);}
        public void stamp(AcNetwork n, NetworkSolveContext ctx){
            String na=ctx.getNodeId(id,a), nb=ctx.getNodeId(id,b);
            if(!na.equals(nb)) n.addElement(new AcImpedanceElement(id+"_z",na,nb,new Complex(0.001,0)));
        }
    }
    static class Cable2 implements ElectricalComponent {
        final String id;
        Cable2(String id){this.id=id;}
        public String getId(){return id;}
        public List<String> getTerminalIds(){return Arrays.asList("phase_a","phase_b","neutral_a","neutral_b");}
        public void stamp(AcNetwork n, NetworkSolveContext ctx){
            String pa=ctx.getNodeId(id,"phase_a"), pb=ctx.getNodeId(id,"phase_b");
            String na=ctx.getNodeId(id,"neutral_a"), nb=ctx.getNodeId(id,"neutral_b");
            if(!pa.equals(pb)) n.addElement(new AcImpedanceElement(id+"_p",pa,pb,new Complex(0.5,0.1)));
            if(!na.equals(nb)) n.addElement(new AcImpedanceElement(id+"_n",na,nb,new Complex(0.001,0)));
        }
    }

    @Test void seriesMeterMeasuresCurrent() {
        ElectricalNetwork net = new ElectricalNetwork();
        net.addComponent(new SourceComponent("gen",230,0));
        net.addComponent(new Cable2("c1"));
        net.addComponent(new PassThrough("meter","phase_a","phase_b"){
            public List<String> getTerminalIds(){return Arrays.asList("phase_a","phase_b","neutral_a","neutral_b");}
            public void stamp(AcNetwork n, NetworkSolveContext ctx){
                String pa=ctx.getNodeId(id,"phase_a"), pb=ctx.getNodeId(id,"phase_b");
                String na=ctx.getNodeId(id,"neutral_a"), nb=ctx.getNodeId(id,"neutral_b");
                if(!pa.equals(pb)) n.addElement(new AcImpedanceElement(id+"_phase",pa,pb,new Complex(0.001,0)));
                if(!na.equals(nb)) n.addElement(new AcImpedanceElement(id+"_neutral",na,nb,new Complex(0.001,0)));
            }
        });
        net.addComponent(new LoadComponent("load",10,0));
        net.connect(new ElectricalTerminal("gen","phase"), new ElectricalTerminal("c1","phase_a"));
        net.connect(new ElectricalTerminal("c1","phase_b"), new ElectricalTerminal("meter","phase_a"));
        net.connect(new ElectricalTerminal("meter","phase_b"), new ElectricalTerminal("load","phase"));
        net.connect(new ElectricalTerminal("gen","neutral"), new ElectricalTerminal("c1","neutral_a"));
        net.connect(new ElectricalTerminal("c1","neutral_b"), new ElectricalTerminal("meter","neutral_a"));
        net.connect(new ElectricalTerminal("meter","neutral_b"), new ElectricalTerminal("load","neutral"));
        AcSolution s = net.solve();
        double i = s.getElementCurrent("meter_phase").magnitude();
        assertTrue(i > 10, "current="+i);
    }
}
