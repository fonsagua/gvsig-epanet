package es.udc.cartolab.gvsig.epanet;

import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.network.LayerParser;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.utils.ComparatorUtils;
import es.udc.cartolab.gvsig.epanet.utils.FixtureLayerFactory;
import es.udc.cartolab.gvsig.epanet.utils.TestProperties;
import es.udc.cartolab.gvsig.shputils.Drivers;

public class LayerSimulationIntegrationTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private LayerParser layerParser;
    private FixtureLayerFactory fixtureFactory;

    @BeforeClass
    public static void setUpBeforeClass() {
	Drivers.initgvSIGDrivers(TestProperties.driversPath);
	Preferences.setBaseformPath("lib/BaseformEpaNetLib-1.0.jar");
    }

    @Before
    public void setUp() throws Exception {
	layerParser = new LayerParser();
	fixtureFactory = new FixtureLayerFactory(temp, layerParser);

    }

    @Test
    public void reservoir_junctionWithDemand() throws Exception {
	fixtureFactory.getReservoirJunctionWithDemand();
	executeTest("reservoir_junction-with-demand");
    }

    @Test
    public void reservoir_tank_junctionWithDemand() throws Exception {
	fixtureFactory.getReservoirTankJunctionWithDemand();
	executeTest("reservoir_tank_junction-with-demand");
    }

    @Test
    public void reservoir_valve_tank_junctionWithDemand() throws Exception {
	fixtureFactory.getReservoirValveTankJunctionWithDemand();
	executeTest("reservoir_valve_tank_junctionWithDemand");
    }

    @Test
    public void valve_over_reservoir() throws Exception {
	fixtureFactory.getValveOverReservoir();
	executeTest("valve_over_reservoir");
    }

    @Test
    public void pumpWithPower() throws Exception {
	fixtureFactory.getPumpWithPower();
	executeTest("pumpWithPower");
    }

    private void executeTest(String patternName) throws Exception {
	fixtureFactory.parseSHPs();
	layerParser.hydraulicSim();

	Object[] actual = ComparatorUtils.parseSHPAfterSimulation(temp);

	checkNodes(actual[0]);
	checkLinks(actual[1]);

    }

    private void checkLinks(Object object) {
	Map<String, LinkWrapper> actuals = (Map<String, LinkWrapper>) object;
	for (LinkWrapper expected : layerParser.getLinks().values()) {
	    LinkWrapper actual = actuals.get(expected.getId());
	    ComparatorUtils.checkLinks(expected, actual);
	}

    }

    private void checkNodes(Object object) {
	Map<String, NodeWrapper> actuals = (Map<String, NodeWrapper>) object;
	for (NodeWrapper expected : layerParser.getNodes().values()) {
	    NodeWrapper actual = actuals.get(expected.getId());
	    ComparatorUtils.checkNodes(expected, actual);
	}
    }
}
