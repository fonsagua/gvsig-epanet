package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.IDCreator;
import es.udc.cartolab.gvsig.epanet.NetworkBuilder;

public class ReservoirLayer extends NodeLayer {

    public ReservoirLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    public NodeWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	ReservoirWrapper reservoir = new ReservoirWrapper(iFeature);
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue totalHead = (IntValue) iFeature.getAttribute(0);
	String id = IDCreator.addNode(iFeature.getID());
	reservoir.createReservoir(id, coordinate.x, coordinate.y,
		totalHead.intValue());
	nb.addReservoir(reservoir);
	return reservoir;
    }

    @Override
    protected int[] getIndexes() {
	// TODO Auto-generated method stub
	return null;
    }

}
