package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Point;
import org.addition.epanet.network.structures.Tank;

import com.iver.cit.gvsig.fmap.core.IFeature;

public class ReservoirWrapper extends NodeWrapper {

    public ReservoirWrapper(IFeature iFeature) {
	super(iFeature);
    }

    public ReservoirWrapper(String id, double x, double y, int totalHead) {
	super();
	createReservoir(id, x, y, totalHead);
    }

    protected void createReservoir(String id, double x, double y, int totalHead) {
	Tank tank = new Tank();
	tank.setId(id);
	tank.setPosition(new Point(x, y));
	tank.setElevation(totalHead);
	setNode(tank);
    }

}
