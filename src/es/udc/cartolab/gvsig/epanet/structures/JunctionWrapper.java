package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Demand;
import org.addition.epanet.network.structures.Node;
import org.addition.epanet.network.structures.Point;

import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.vividsolutions.jts.geom.Coordinate;

public class JunctionWrapper extends NodeWrapper {

    public JunctionWrapper(IFeature iFeature) {
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue elevation = (IntValue) iFeature.getAttribute(0);
	IntValue demand = (IntValue) iFeature.getAttribute(1);

	createJunction("", coordinate.x, coordinate.y, elevation.intValue(),
		demand.intValue());
    }

    public JunctionWrapper(String id, double x, double y, int elevation,
	    int baseDemand) {
	createJunction(id, x, y, elevation, baseDemand);
    }

    private void createJunction(String id, double x, double y, int elevation,
	    int baseDemand) {
	// TODO: Probablemente habría que hacer dos métodos, 1 para los nodos
	// con demanda y otro para los que no tiene demanda. Chequear
	// parseJunction
	Node node = new Node();
	node.setPosition(new Point(x, y));
	node.setElevation(elevation);
	Demand demand = new Demand(baseDemand, null);
	node.getDemand().add(demand);
	node.setInitDemand(baseDemand);
	node.setId(id);
	setNode(node);
    }

}
