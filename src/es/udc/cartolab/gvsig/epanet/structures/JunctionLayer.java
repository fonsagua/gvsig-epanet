package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.IDCreator;
import es.udc.cartolab.gvsig.epanet.NetworkBuilder;

public class JunctionLayer extends NodeLayer {

    public JunctionLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    public NodeWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	JunctionWrapper junction = new JunctionWrapper(iFeature);
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue elevation = (IntValue) iFeature.getAttribute(0);
	IntValue demand = (IntValue) iFeature.getAttribute(1);
	String id = IDCreator.addNode(iFeature.getID());
	junction.createJunction(id, coordinate.x, coordinate.y,
		elevation.intValue(), demand.intValue());
	nb.addJunction(junction);
	return junction;
    }

}
