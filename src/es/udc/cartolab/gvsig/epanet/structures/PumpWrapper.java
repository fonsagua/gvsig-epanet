package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Link.LinkType;
import org.addition.epanet.network.structures.Link.StatType;
import org.addition.epanet.network.structures.Pump;

import com.iver.cit.gvsig.fmap.core.IFeature;

public class PumpWrapper extends LinkWrapper {

    public PumpWrapper(IFeature feature) {
	super(feature);
    }

    public PumpWrapper(String id, NodeWrapper startNode, NodeWrapper endNode,
	    double power) {
	super(null);
	createPump(id, startNode, endNode, power);
    }

    protected void createPump(String id, NodeWrapper startNode,
	    NodeWrapper endNode, double power) {
	Pump pump = new Pump();
	pump.setId(id);
	// Link attributes
	pump.setFirst(startNode.getNode());
	pump.setSecond(endNode.getNode());
	pump.setDiameter(0);
	pump.setLenght(0.0d);
	pump.setRoughness(1.0d);
	pump.setKm(0.0d);
	pump.setKb(0.0d);
	pump.setKw(0.0d);
	pump.setType(LinkType.PUMP);
	pump.setStatus(StatType.OPEN);
	pump.setReportFlag(false);

	// Pump attributes
	pump.setHcurve(null);
	pump.setEcurve(null);
	pump.setUpat(null);
	pump.setEcost(0.0d);
	pump.setEpat(null);
	pump.setPtype(Pump.Type.CONST_HP);
	pump.setKm(power);

	setLink(pump);
    }

}
