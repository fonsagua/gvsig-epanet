package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.engine.values.DoubleValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.epanet.IDCreator;
import es.udc.cartolab.gvsig.epanet.NetworkBuilder;

public class PipeLayer extends LinkLayer {

    public PipeLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    public LinkWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	NodeFinder nodeFinder = new NodeFinder(nb.getNodes(), nb.getAuxNodes());
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
	nb.addPipe(pipe);
	return pipe;
    }

}
