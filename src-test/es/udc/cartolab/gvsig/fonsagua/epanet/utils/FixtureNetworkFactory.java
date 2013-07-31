package es.udc.cartolab.gvsig.fonsagua.epanet.utils;

import java.util.HashMap;
import java.util.Map;

import org.addition.epanet.network.structures.Curve.CurveType;

import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;
import es.udc.cartolab.gvsig.epanet.structures.JunctionWrapper;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PipeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PumpWrapper;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirWrapper;
import es.udc.cartolab.gvsig.epanet.structures.TankWrapper;
import es.udc.cartolab.gvsig.epanet.structures.ValveWrapper;

/* Don't use in this class the addXXX(NodeWrapper) or addXXX(LinkWrapper) methods.
 * Those methods does not creates a new object so the fixture object and the real object 
 * will be the same, and introduce errors in the test*/
public class FixtureNetworkFactory {

    private NetworkBuilder nb;
    private final Map<String, NodeWrapper> nodesWrapper;
    private final Map<String, LinkWrapper> linksWrapper;

    public FixtureNetworkFactory(NetworkBuilder nb) {
	this.nb = nb;
	nodesWrapper = new HashMap<String, NodeWrapper>();
	linksWrapper = new HashMap<String, LinkWrapper>();
    }

    public Map<String, LinkWrapper> getLinks() {
	return linksWrapper;
    }

    public Map<String, NodeWrapper> getNodes() {
	return nodesWrapper;
    }

    private void addJunction(String id, double x, double y, double elevation,
	    double baseDemand, double pressure, double head, double demand) {
	JunctionWrapper node = new JunctionWrapper(id, 200.00, 8500.00, 50, 1);
	setResults(pressure, head, demand, node);
	nodesWrapper.put(node.getId(), node);
	nb.addJunction(id, x, y, elevation, baseDemand);
    }

    private void addReservoir(String id, double x, double y, double totalHead,
	    double pressure, double head, double demand) {
	ReservoirWrapper node = new ReservoirWrapper(id, x, y, totalHead);
	setResults(pressure, head, demand, node);
	nodesWrapper.put(node.getId(), node);
	nb.addReservoir(id, x, y, totalHead);
    }

    private void addTank(String id, double x, double y, double elevation,
	    int initLevel, int minLevel, int maxLevel, double diameter,
	    double pressure, double head, double demand) {
	TankWrapper node = new TankWrapper(id, x, y, elevation, initLevel,
		minLevel, maxLevel, diameter);
	setResults(pressure, head, demand, node);
	nodesWrapper.put(node.getId(), node);
	nb.addTank(id, x, y, elevation, initLevel, minLevel, maxLevel, diameter);
    }

    private void setResults(double pressure, double head, double demand,
	    NodeWrapper node) {
	node.setPressure(pressure);
	node.setHead(head);
	node.setDemand(demand);
    }

    private void addPipe(String id, String startNode, String endNode,
	    double len, double diameter, double roughness, double flow,
	    double velocity, double unitheadloss, double frictionfactor) {
	PipeWrapper link = new PipeWrapper(id, nb.getNodes().get(startNode), nb
		.getNodes().get(endNode), len, diameter, roughness);

	setResults(flow, velocity, unitheadloss, frictionfactor, link);
	linksWrapper.put(link.getId(), link);
	nb.addPipe(id, startNode, endNode, len, diameter, roughness);
    }

    private void addFlowControlValve(String id, String startNode,
	    String endNode, double diameter, double flow, double expFlow,
	    double velocity, double unitheadloss, double frictionfactor) {
	LinkWrapper link = new ValveWrapper(id, nb.getNodes().get(startNode),
		nb.getNodes().get(endNode), diameter, flow);
	setResults(expFlow, velocity, unitheadloss, frictionfactor, link);
	linksWrapper.put(link.getId(), link);
	nb.addFlowControlValve(id, startNode, endNode, diameter, flow);
    }

    private void addPump(String id, String startNode, String endNode,
	    double power, double flow, double velocity, double unitheadloss,
	    double frictionfactor) {
	PumpWrapper link = new PumpWrapper(id, nb.getNodes().get(startNode), nb
		.getNodes().get(endNode), power);
	setResults(flow, velocity, unitheadloss, frictionfactor, link);
	linksWrapper.put(link.getId(), link);
	nb.addPump(id, startNode, endNode, power);

    }

    private void setResults(double flow, double velocity, double unitheadloss,
	    double frictionfactor, LinkWrapper link) {
	link.setFlow(flow);
	link.setVelocity(velocity);
	link.setUnitHeadLoss(unitheadloss);
	link.setFrictionFactor(frictionfactor);

    }

    public void getReservoirJunctionWithDemand() {
	addJunction("1", 200.00, 8500.00, 50, 1, 50, 100, 1);
	addReservoir("2", -800.00, 8500.00, 100, 0, 100, -1);
	addPipe("1", "2", "1", 1000, 300, 0.1, 1, 0.01, 0.00138, 0.04);
    }

    public void getReservoirTankJuctionWithDemand() {
	addJunction("1", 207.01, 8428.87, 20, 1, 33.46, 53.46, 1);
	addReservoir("2", -1321.66, 8460.72, 100, 0, 100, -12.14);
	addTank("3", -557.32, 8704.88, 50, 5, 0, 10, 5, 5, 55, 11.14);
	addPipe("1", "1", "3", 200, 50, 0.1, 1, 0.51, 7.70, 0.03);
	addPipe("2", "2", "3", 1000, 90, 0.1, 12.14, 1.91, 45, 0.02);
    }

    public void getReservoirValveTankJuctionWithDemand() {
	addJunction("1", 4145.44, 7664.54, 20, 1, 33.46, 53.46, 1);
	addJunction("4", -1406.58, 7728.24, 90, 0, 7.17, 97.17, 0);
	addJunction("5", -1119.96, 7728.24, 90, 0, -9.52, 80.48, 0);
	addReservoir("2", -1884.29, 7738.85, 100, 0, 100, -2);
	addTank("3", 1204.88, 8046.71, 50, 5, 0, 10, 5, 5, 55, 1);
	addPipe("2", "5", "3", 900, 50, 0.1, 2, 1.02, 28.31, 0.03);
	addPipe("3", "3", "1", 200, 50, 0.1, 1, 0.51, 7.70, 0.03);
	addPipe("4", "2", "4", 100, 50, 0.1, 2, 1.02, 28.31, 0.03);
	addFlowControlValve("1", "4", "5", 90, 2, 2, 0.31, 16.69, 0);
    }

    public void getPumpWithPower() {
	addJunction("1", 4145.448, 7664.54, 80, 1, 43.46, 123.46, 1);
	addJunction("4", -1406.58, 7728.24, 90, 0, 7.29, 97.29, 0);
	addJunction("5", -1119.96, 7728.24, 90, 0, 59.43, 149.43, 0);
	addReservoir("2", -1884.29, 7738.85, 100, 0, 100, -1.96);
	addTank("3", 1204.88, 8046.71, 120, 5, 0, 10, 5, 5, 125, 0.96);
	addPipe("2", "5", "3", 900, 50, 0.1, 1.96, 1, 27.15, 0.03);
	addPipe("3", "3", "1", 200, 50, 0.1, 1, 0.51, 7.70, 0.03);
	addPipe("4", "2", "4", 100, 50, 0.1, 1.96, 1, 27.15, 0.03);
	addPump("1", "4", "5", 1, 1.96, 0, -52.15, 0);
    }

    public void getPumpWithCurve() {
	nb.addJunction("2", -1406.58, 7728.24, 90, 0);
	nb.addJunction("3", -1119.96, 7728.24, 90, 0);
	nb.addJunction("4", 4145.448, 7664.54, 80, 1);
	nb.addReservoir("1", -1884.29, 7738.85, 100);
	nb.addTank("5", 1204.88, 8046.71, 120, 5, 0, 10, 5);
	nb.addPipe("1", "3", "5", 900, 50, 0.1);
	nb.addPipe("2", "5", "4", 200, 50, 0.1);
	nb.addPipe("3", "1", "2", 100, 50, 0.1);
	nb.getHeadCurve("1", CurveType.H_CURVE, 2.71, 60);
	nb.getPumpWithCurve("4", "2", "3", "1");
    }

}
