package es.udc.cartolab.gvsig.fonsagua.epanet;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import es.udc.cartolab.gvsig.epanet.BaseformWrapper;
import es.udc.cartolab.gvsig.epanet.EpanetWrapper;
import es.udc.cartolab.gvsig.epanet.LayerParser;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.ComparatorUtils;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.FixtureLayerFactory;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.TestProperties;
import es.udc.cartolab.gvsig.shputils.Drivers;

public class LayerParserIntegrationTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private static EpanetWrapper epanet;
    private static BaseformWrapper baseform;

    private LayerParser layerParser;
    private FixtureLayerFactory fixtureFactory;

    @BeforeClass
    public static void setUpBeforeClass() {
	Drivers.initgvSIGDrivers(TestProperties.driversPath);
	epanet = new EpanetWrapper(TestProperties.epanetPath);
	baseform = new BaseformWrapper("lib/BaseformEpaNetLib-1.0.jar");
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

	File inp = temp.newFile("foo.inp");
	layerParser.createInpFile(inp);

	// Epanet
	String actualRPT[] = epanet.execute(inp.getAbsolutePath());
	final File expectedRPT = new File("fixtures/" + patternName + ".rpt");
	assertTrue(ComparatorUtils.rptComparator(expectedRPT, new File(
		actualRPT[0])));

	// Baseform
	String actualRPT2[] = baseform.execute(inp.getAbsolutePath());
	assertTrue(FileUtils.contentEquals(new File(actualRPT2[0]), new File(
		"fixtures/" + patternName + ".inp.links.out")));
	assertTrue(FileUtils.contentEquals(new File(actualRPT2[1]), new File(
		"fixtures/" + patternName + ".inp.nodes.out")));
    }
}
