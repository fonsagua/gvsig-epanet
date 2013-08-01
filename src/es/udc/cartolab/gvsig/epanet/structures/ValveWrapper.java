package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Link.LinkType;
import org.addition.epanet.network.structures.Link.StatType;
import org.addition.epanet.network.structures.Valve;

import com.iver.cit.gvsig.fmap.core.IFeature;

public class ValveWrapper extends LinkWrapper {

    public ValveWrapper(IFeature feature) {
	super(feature);
    }

    public ValveWrapper(String id, NodeWrapper startNode, NodeWrapper endNode,
	    double diameter, double setting) {
	super();
	createValve(id, startNode, endNode, diameter, setting);
    }

    protected void createValve(String id, NodeWrapper startNode,
	    NodeWrapper endNode, double diameter, double setting) {
	// TODO: MinorLoss; Checks from InpParser
	Valve valve = new Valve();
	valve.setId(id);
	valve.setFirst(startNode.getNode());
	valve.setSecond(endNode.getNode());
	valve.setDiameter(diameter);
	valve.setLenght(0.0d);
	valve.setRoughness(setting);
	valve.setKm(0.0);
	valve.setKb(0.0d);
	valve.setKw(0.0d);
	valve.setType(LinkType.FCV);
	valve.setStatus(StatType.ACTIVE);

	setLink(valve);
    }
}
