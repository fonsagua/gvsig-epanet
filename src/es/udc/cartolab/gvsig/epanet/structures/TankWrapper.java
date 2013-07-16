package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Point;
import org.addition.epanet.network.structures.Tank;

import com.iver.cit.gvsig.fmap.core.IFeature;

public class TankWrapper extends NodeWrapper {

    public TankWrapper(IFeature feature) {
	super(feature);
    }

    public TankWrapper(String id, double x, double y, int elevation,
	    int initLevel, int minLevel, int maxLevel, double diameter) {
	super();
	createTank(id, x, y, elevation, initLevel, minLevel, maxLevel, diameter);
    }

    protected void createTank(String id, double x, double y, int elevation,
	    int initLevel, int minLevel, int maxLevel, double diameter) {
	// TODO: MinVol
	Tank tank = new Tank();
	tank.setId(id);
	tank.setPosition(new Point(x, y));
	tank.setElevation(elevation);
	tank.setH0(initLevel);
	tank.setHmin(minLevel);
	tank.setHmax(maxLevel);
	tank.setArea(diameter);
	tank.setMixModel(Tank.MixType.MIX1); // debería estar en initTanks
	setNode(tank);
    }
}
