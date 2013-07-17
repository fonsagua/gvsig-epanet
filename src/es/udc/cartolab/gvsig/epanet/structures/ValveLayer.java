package es.udc.cartolab.gvsig.epanet.structures;

import java.util.Map;

import com.hardcode.gdbms.engine.values.DoubleValue;
import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.IDCreator;
import es.udc.cartolab.gvsig.epanet.NetworkBuilder;

public class ValveLayer extends LinkLayer {

    public ValveLayer(FLyrVect layer) {
	super(layer);

    }

    @Override
    public LinkWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	Map<String, NodeWrapper> auxNodes = nb.getAuxNodes();
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue elevation = (IntValue) iFeature.getAttribute(0);
	DoubleValue diameter = (DoubleValue) iFeature.getAttribute(1);
	DoubleValue flow = (DoubleValue) iFeature.getAttribute(2);
	int baseDemand = 0;

	String startNodeId = IDCreator.addValveNode(iFeature.getID());
	NodeWrapper startNode = new JunctionWrapper(startNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(startNodeId, startNode);

	String endNodeId = IDCreator.addValveNode(iFeature.getID());
	NodeWrapper endNode = new JunctionWrapper(endNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(endNodeId, endNode);

	String id = IDCreator.addValveLink(iFeature.getID());
	ValveWrapper valve = new ValveWrapper(iFeature);
	valve.createValve(id, startNode, endNode, diameter.intValue(),
		flow.intValue());
	nb.addFlowControlValve(valve);
	return valve;
    }

}
