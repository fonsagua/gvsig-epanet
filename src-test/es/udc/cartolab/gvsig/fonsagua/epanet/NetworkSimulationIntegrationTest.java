package es.udc.cartolab.gvsig.fonsagua.epanet;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import es.udc.cartolab.gvsig.epanet.NetworkBuilder;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;

public class NetworkSimulationIntegrationTest {

    private NetworkBuilder nb;
    private FixtureNetworkFactory fixtureFactory;

    @Before
    public void setUp() throws Exception {
	nb = new NetworkBuilder();
	fixtureFactory = new FixtureNetworkFactory(nb);
    }

    @Test
    public void reservoir_junctionWithDemand() throws Exception {
	fixtureFactory.getReservoirJunctionWithDemand();
	executeTest("reservoir_junction-with-demand");
    }

    @Test
    public void reservoir_tank_junctionWithDemand() throws Exception {
	fixtureFactory.getReservoirTankJuctionWithDemand();
	executeTest("reservoir_tank_junction-with-demand");
    }

    @Test
    public void reservoir__valve_tank_junctionWithDemand() throws Exception {
	fixtureFactory.getReservoirValveTankJuctionWithDemand();
	executeTest("reservoir_valve_tank_junctionWithDemand");
    }

    @Test
    public void pumpWithPower() throws Exception {
	fixtureFactory.getPumpWithPower();
	executeTest("pumpWithPower");
    }

    private void executeTest(String patternName) throws Exception {
	nb.hydraulicSim();
	Map<String, NodeWrapper> nodes = nb.getNodes();
	Map<String, LinkWrapper> links = nb.getLinks();
	Map<String, NodeWrapper> expNodes = fixtureFactory.getNodes();
	Map<String, LinkWrapper> expLinks = fixtureFactory.getLinks();
	for (String id : expNodes.keySet()) {
	    checkNodes(expNodes.get(id), nodes.get(id));
	}

	for (String id : expLinks.keySet()) {
	    checkLinks(expLinks.get(id), links.get(id));
	}
    }

    private void checkLinks(LinkWrapper expected, LinkWrapper actual) {
	double expValue = round(expected.getFlow());
	assertEquals(expValue, actual.getFlow(), delta(expValue));

	expValue = round(expected.getVelocity());
	assertEquals(expValue, actual.getVelocity(), delta(expValue));

	expValue = round(expected.getUnitHeadLoss());
	assertEquals(expValue, actual.getUnitHeadLoss(), delta(expValue));

	expValue = round(expected.getFrictionFactor());
	assertEquals(expValue, actual.getFrictionFactor(), delta(expValue));
    }

    private void checkNodes(NodeWrapper expected, NodeWrapper actual) {
	double value = round(expected.getPressure());
	assertEquals(value, actual.getPressure(), delta(value));

	value = round(expected.getHead());
	assertEquals(value, actual.getHead(), delta(value));

	value = round(expected.getDemand());
	assertEquals(value, actual.getDemand(), delta(value));
    }

    private double round(double value) {
	return Math.round(value * 100) / 100d;
    }

    private double delta(double value) {
	return Math.abs(0.01 * value);
    }
}
