package es.udc.cartolab.gvsig.fonsagua.epanet;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import es.udc.cartolab.gvsig.epanet.network.BaseformWrapper;
import es.udc.cartolab.gvsig.epanet.network.EpanetWrapper;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.ComparatorUtils;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.FixtureNetworkFactory;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.TestProperties;

public class NetworkBuilderIntegrationTest {

    private static EpanetWrapper epanet;

    private static BaseformWrapper baseform;

    private FixtureNetworkFactory fixtureFactory;

    private NetworkBuilder nb;

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @BeforeClass
    public static void setUpBeforeClass() {
	epanet = new EpanetWrapper(TestProperties.epanetPath);
	baseform = new BaseformWrapper("lib/BaseformEpaNetLib-1.0.jar");
    }

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

    // TODO: check inp
    @Test
    public void reservoir_tank_junctionWithDemand() throws Exception {
	fixtureFactory.getReservoirTankJuctionWithDemand();
	executeTest("reservoir_tank_junction-with-demand");
    }

    // TODO: check inp
    @Test
    public void reservoir__valve_tank_junctionWithDemand() throws Exception {
	fixtureFactory.getReservoirValveTankJuctionWithDemand();
	executeTest("reservoir_valve_tank_junctionWithDemand");
    }

    // TODO: check inp
    @Test
    public void pumpWithPower() throws Exception {
	fixtureFactory.getPumpWithPower();
	executeTest("pumpWithPower");
    }

    // TODO: check inp
    @Test
    public void pumpWithCurve() throws Exception {
	fixtureFactory.getPumpWithCurve();
	executeTest("pumpWithCurve");
    }

    private void executeTest(String patternName) throws Exception {

	File inp = temp.newFile("foo.inp");
	nb.createInpFile(inp);

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
