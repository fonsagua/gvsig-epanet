package es.udc.cartolab.gvsig.fonsagua.epanet;

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
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.ComparatorUtils;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.FixtureLayerFactory;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.TestProperties;
import es.udc.cartolab.gvsig.shputils.Drivers;

public class AcceptanceTest {

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
	executeTest();
    }

    private void executeTest() throws Exception {
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
