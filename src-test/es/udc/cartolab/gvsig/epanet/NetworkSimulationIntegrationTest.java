package es.udc.cartolab.gvsig.epanet;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.utils.ComparatorUtils;
import es.udc.cartolab.gvsig.epanet.utils.FixtureNetworkFactory;

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
	    ComparatorUtils.checkNodes(expNodes.get(id), nodes.get(id));
	}

	for (String id : expLinks.keySet()) {
	    ComparatorUtils.checkLinks(expLinks.get(id), links.get(id));
	}
    }

}
