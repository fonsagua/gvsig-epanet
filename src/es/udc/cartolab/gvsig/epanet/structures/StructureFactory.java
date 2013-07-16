package es.udc.cartolab.gvsig.epanet.structures;

import java.util.Map;

import com.hardcode.gdbms.engine.values.DoubleValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.epanet.IDCreator;

public class StructureFactory {

    private Map<String, LinkWrapper> links;
    private Map<String, NodeWrapper> nodes;
    private IDCreator idCreator;
    private NodeFinder nodeFinder;

    public StructureFactory(Map<String, LinkWrapper> links,
	    Map<String, NodeWrapper> nodes) {
	this.links = links;
	this.nodes = nodes;
	idCreator = new IDCreator();
	nodeFinder = new NodeFinder(nodes);
    }

    public LinkWrapper getPipe(IFeature iFeature) {
	String id = idCreator.addLink(iFeature.getID());
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
}
