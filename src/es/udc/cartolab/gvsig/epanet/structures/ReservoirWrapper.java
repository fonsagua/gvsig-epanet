package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Point;
import org.addition.epanet.network.structures.Tank;

import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.vividsolutions.jts.geom.Coordinate;

public class ReservoirWrapper extends NodeWrapper {

    public ReservoirWrapper(IFeature iFeature) {
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue totalHead = (IntValue) iFeature.getAttribute(0);
	createReservoir("", coordinate.x, coordinate.y, totalHead.intValue());
    }

    public ReservoirWrapper(String id, double x, double y, int totalHead) {
	createReservoir(id, x, y, totalHead);
    }

    private void createReservoir(String id, double x, double y, int totalHead) {
	Tank tank = new Tank();
	tank.setId(id);
	tank.setPosition(new Point(x, y));
	tank.setElevation(totalHead);
	setNode(tank);
    }

}
