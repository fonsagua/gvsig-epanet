package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.engine.values.DoubleValue;
import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.IDCreator;
import es.udc.cartolab.gvsig.epanet.NetworkBuilder;

public class TankLayer extends NodeLayer {

    public TankLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    public NodeWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	TankWrapper tank = new TankWrapper(iFeature);

	String id = IDCreator.addNode(iFeature.getID());
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue elevation = (IntValue) iFeature.getAttribute(0);
	IntValue initLevel = (IntValue) iFeature.getAttribute(1);
	IntValue minLevel = (IntValue) iFeature.getAttribute(2);
	IntValue maxLevel = (IntValue) iFeature.getAttribute(3);
	DoubleValue diameter = (DoubleValue) iFeature.getAttribute(4);
	tank.createTank(id, coordinate.x, coordinate.y, elevation.intValue(),
		initLevel.intValue(), minLevel.intValue(), maxLevel.intValue(),
		diameter.doubleValue());
	nb.addTank(tank);
	return tank;
    }

}
