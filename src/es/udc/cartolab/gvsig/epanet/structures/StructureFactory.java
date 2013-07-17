package es.udc.cartolab.gvsig.epanet.structures;

import java.util.Map;

import com.hardcode.gdbms.engine.values.DoubleValue;
import com.hardcode.gdbms.engine.values.IntValue;
import com.hardcode.gdbms.engine.values.StringValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.epanet.IDCreator;

public class StructureFactory {

    private final Map<String, LinkWrapper> links;
    private final Map<String, NodeWrapper> nodes;
    private final Map<String, NodeWrapper> auxNodes;
    private NodeFinder nodeFinder;

    public StructureFactory(Map<String, LinkWrapper> links,
	    Map<String, NodeWrapper> nodes, Map<String, NodeWrapper> auxNodes) {
	this.links = links;
	this.nodes = nodes;
	this.auxNodes = auxNodes;
	nodeFinder = new NodeFinder(nodes, auxNodes);
    }

    public LinkWrapper getPipe(IFeature iFeature) {
	String id = IDCreator.addLink(iFeature.getID());
	PipeWrapper pipe = new PipeWrapper(iFeature);
	DoubleValue diameter = (DoubleValue) iFeature.getAttribute(0);
	DoubleValue roughness = (DoubleValue) iFeature.getAttribute(1);

	Geometry geom = iFeature.getGeometry().toJTSGeometry();
	Coordinate[] coordinates = geom.getCoordinates();

	NodeWrapper startNode = nodeFinder
		.getNodeForInitialPoint(coordinates[0]);
	NodeWrapper endNode = nodeFinder.getNodeForEndPoint(coordinates[1]);

	pipe.createPipe(id, startNode, endNode, geom.getLength(),
		diameter.doubleValue(), roughness.doubleValue());
	return pipe;
    }

    public LinkWrapper getPipe(String id, String startNode, String endNode,
	    double len, double diameter, double roughness) {
	PipeWrapper pipe = new PipeWrapper(null);

	pipe.createPipe(id, nodes.get(startNode), nodes.get(endNode), len,
		diameter, roughness);
	return pipe;
    }

    public ValveWrapper getFCV(IFeature iFeature) {
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
	return valve;

    }

    public PumpWrapper getPump(IFeature iFeature) {

	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue elevation = (IntValue) iFeature.getAttribute(0);
	StringValue type = (StringValue) iFeature.getAttribute(1);
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

	return pump;
    }
}
