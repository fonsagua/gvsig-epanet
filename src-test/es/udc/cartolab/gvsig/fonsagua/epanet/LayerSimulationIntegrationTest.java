package es.udc.cartolab.gvsig.fonsagua.epanet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import es.udc.cartolab.gvsig.epanet.network.LayerParser;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.FixtureLayerFactory;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.TestProperties;
import es.udc.cartolab.gvsig.shputils.Drivers;

public class LayerSimulationIntegrationTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private LayerParser layerParser;
    private FixtureLayerFactory fixtureFactory;

    @BeforeClass
    public static void setUpBeforeClass() {
	Drivers.initgvSIGDrivers(TestProperties.driversPath);
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
    public void pumpWithPower() throws Exception {
	fixtureFactory.getPumpWithPower();
	executeTest("pumpWithPower");
    }

    private void executeTest(String patternName) throws Exception {
	fixtureFactory.parseSHPs();
	layerParser.hydraulicSim();
    }
}
