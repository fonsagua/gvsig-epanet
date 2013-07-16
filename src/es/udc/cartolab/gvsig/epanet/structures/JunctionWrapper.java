package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Demand;
import org.addition.epanet.network.structures.Node;
import org.addition.epanet.network.structures.Point;

import com.iver.cit.gvsig.fmap.core.IFeature;

public class JunctionWrapper extends NodeWrapper {

    public JunctionWrapper(IFeature iFeature) {
	super(iFeature);
    }

    public JunctionWrapper(String id, double x, double y, double elevation,
	    double baseDemand) {
	super();
	createJunction(id, x, y, elevation, baseDemand);
    }

    protected void createJunction(String id, double x, double y,
	    double elevation, double baseDemand) {
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
