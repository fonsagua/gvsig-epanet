package es.udc.cartolab.gvsig.epanet.structures;

import java.util.Map;

import com.hardcode.gdbms.engine.values.IntValue;
import com.hardcode.gdbms.engine.values.StringValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.IDCreator;
import es.udc.cartolab.gvsig.epanet.NetworkBuilder;

public class PumpLayer extends LinkLayer {

    public PumpLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    public LinkWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	Map<String, NodeWrapper> auxNodes = nb.getAuxNodes();
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue elevation = (IntValue) iFeature.getAttribute(0);
	// StringValue type = (StringValue) iFeature.getAttribute(1);
	StringValue value = (StringValue) iFeature.getAttribute(2);
	int baseDemand = 0;

	String startNodeId = IDCreator.addPumpNode(iFeature.getID());
	NodeWrapper startNode = new JunctionWrapper(startNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(startNodeId, startNode);

	String endNodeId = IDCreator.addPumpNode(iFeature.getID());
	NodeWrapper endNode = new JunctionWrapper(endNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(endNodeId, endNode);

	String id = IDCreator.addPumpLink(iFeature.getID());
	double power = Double.parseDouble(value.getValue());
	PumpWrapper pump = new PumpWrapper(iFeature);
	pump.createPump(id, startNode, endNode, power);
	nb.addPump(pump);
	return pump;
    }

}
