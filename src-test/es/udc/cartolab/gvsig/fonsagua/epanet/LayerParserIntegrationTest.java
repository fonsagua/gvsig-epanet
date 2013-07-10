package es.udc.cartolab.gvsig.fonsagua.epanet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.hardcode.driverManager.DriverLoadException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;

import es.udc.cartolab.gvsig.shputils.SHPFactory;

public class LayerParserIntegrationTest {

    private static EpanetWrapper epanet;
    private static BaseformWrapper baseform;
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private LayerParser layerParser;

    @BeforeClass
    public static void setUpBeforeClass() {
	initgvSIGDrivers();
	epanet = new EpanetWrapper(TestProperties.epanetPath);
	baseform = new BaseformWrapper("binaries/BaseformEpaNetLib-1.0.jar");
    }

    private static void initgvSIGDrivers() {

	final String fwAndamiDriverPath = TestProperties.driversPath;

	final File baseDriversPath = new File(fwAndamiDriverPath);
	if (!baseDriversPath.exists()) {
	    throw new RuntimeException("Can't find drivers path: "
		    + fwAndamiDriverPath);
	}

	LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
	if (LayerFactory.getDM().getDriverNames().length < 1) {
	    throw new RuntimeException("Can't find drivers in path: "
		    + fwAndamiDriverPath);
	}
	LayerFactory.setWritersPath(baseDriversPath.getAbsolutePath());
	if (LayerFactory.getWM().getWriterNames().length < 1) {
	    throw new RuntimeException("Can't find writers in path: "
		    + fwAndamiDriverPath);
	}
    }

    @Before
    public void setUp() throws Exception {
	layerParser = new LayerParser();
    }

    @Test
    public void checkDistance() {
	IGeometry a = ShapeFactory.createPoint2D(200, 8500);
	IGeometry b = ShapeFactory.createPoint2D(-800, 8500);
	assertEquals(1000, a.toJTSGeometry().distance(b.toJTSGeometry()), 1);
    }

    @Test
    public void reservoir_junctionWithDemand() throws Exception {
	getReservoirJunctionWithDemand();
	parseSHPs();

	executeTest("reservoir_junction-with-demand");
    }

    private void getReservoirJunctionWithDemand() throws Exception {
	IFeature junctionFeat = FixtureSHPFactory.createJunctionFeature(200,
		8500, 50, 1);
	File file = temp.newFile("junctions.shp");
	FixtureSHPFactory.createJunctionShp(file,
		new IFeature[] { junctionFeat });

	IFeature reservoirFeat = FixtureSHPFactory.createReservoirFeature(-800,
		8500, 100);
	file = temp.newFile("reservoirs.shp");
	FixtureSHPFactory.createReservoirShp(file,
		new IFeature[] { reservoirFeat });

	IFeature pipeFeat = FixtureSHPFactory.createPipeFeature(junctionFeat,
		reservoirFeat, 300, 0.1);
	file = temp.newFile("pipes.shp");
	FixtureSHPFactory.createPipeShp(file, new IFeature[] { pipeFeat });
    }

    @Test
    public void reservoir_tank_junctionWithDemand() throws Exception {
	getReservoirTankJunctionWithDemand();
	parseSHPs();
	executeTest("reservoir_tank_junction-with-demand");
    }

    private void getReservoirTankJunctionWithDemand() throws Exception {

	IFeature junctionFeat = FixtureSHPFactory.createJunctionFeature(200,
		8500, 20, 1);
	File file = temp.newFile("junctions.shp");
	FixtureSHPFactory.createJunctionShp(file,
		new IFeature[] { junctionFeat });

	IFeature reservoirFeat = FixtureSHPFactory.createReservoirFeature(
		-1000, 8500, 100);
	file = temp.newFile("reservoirs.shp");
	FixtureSHPFactory.createReservoirShp(file,
		new IFeature[] { reservoirFeat });

	IFeature tankFeat = FixtureSHPFactory.createTankFeature(0.0, 8500.0,
		50, 5, 0, 10, 5);
	file = temp.newFile("tanks.shp");
	FixtureSHPFactory.createTankShp(file, new IFeature[] { tankFeat });

	IFeature pipeFeat1 = FixtureSHPFactory.createPipeFeature(junctionFeat,
		tankFeat, 50, 0.1);
	IFeature pipeFeat2 = FixtureSHPFactory.createPipeFeature(reservoirFeat,
		tankFeat, 90, 0.1);
	file = temp.newFile("pipes.shp");
	FixtureSHPFactory.createPipeShp(file, new IFeature[] { pipeFeat1,
		pipeFeat2 });

    }

    @Test
    public void reservoir_valve_tank_junctionWithDemand() throws Exception {
	getReservoirValveTankJunctionWithDemand();
	parseSHPs();
	executeTest("reservoir_valve_tank_junctionWithDemand");
    }

    private void getReservoirValveTankJunctionWithDemand() throws Exception {
	IFeature n1 = FixtureSHPFactory.createReservoirFeature(0, 8500, 100);
	File file = temp.newFile("reservoirs.shp");
	FixtureSHPFactory.createReservoirShp(file, new IFeature[] { n1 });

	double x = 100;
	double y = 8500;
	IFeature l4 = FixtureSHPFactory.createFCVFeature(x, y, 90, 90, 2);
	file = temp.newFile("valves.shp");
	FixtureSHPFactory.createFCVShp(file, new IFeature[] { l4 });

	IFeature n4 = FixtureSHPFactory
		.createJunctionFeature(1200, 8500, 20, 1);
	file = temp.newFile("junctions.shp");
	FixtureSHPFactory.createJunctionShp(file, new IFeature[] { n4 });

	IFeature n5 = FixtureSHPFactory.createTankFeature(1000, 8500.0, 50, 5,
		0, 10, 5);
	file = temp.newFile("tanks.shp");
	FixtureSHPFactory.createTankShp(file, new IFeature[] { n5 });

	IFeature l1 = FixtureSHPFactory.createPipeFeature(l4, n5, 50, 0.1);
	IFeature l2 = FixtureSHPFactory.createPipeFeature(n5, n4, 50, 0.1);
	IFeature l3 = FixtureSHPFactory.createPipeFeature(n1, l4, 50, 0.1);
	file = temp.newFile("pipes.shp");
	FixtureSHPFactory.createPipeShp(file, new IFeature[] { l1, l2, l3 });

    }

    private void parseSHPs() throws DriverLoadException, Exception {
	FLyrVect reservoirs = SHPFactory.getFLyrVectFromSHP(new File(temp
		.getRoot().getAbsoluteFile()
		+ File.separator
		+ "reservoirs.shp"));
	layerParser.addReservoirs(reservoirs);

	FLyrVect valves = SHPFactory.getFLyrVectFromSHP(new File(temp.getRoot()
		.getAbsoluteFile() + File.separator + "valves.shp"));
	if (valves != null) {
	    layerParser.addValves(valves);
	}

	FLyrVect junctions = SHPFactory
		.getFLyrVectFromSHP(new File(temp.getRoot().getAbsoluteFile()
			+ File.separator + "junctions.shp"));
	layerParser.addJunctions(junctions);

	FLyrVect tanks = SHPFactory.getFLyrVectFromSHP(new File(temp.getRoot()
		.getAbsoluteFile() + File.separator + "tanks.shp"));
	if (tanks != null) {
	    layerParser.addTanks(tanks);
	}
	FLyrVect pipes = SHPFactory.getFLyrVectFromSHP(new File(temp.getRoot()
		.getAbsoluteFile() + File.separator + "pipes.shp"));
	layerParser.addPipes(pipes);

    }

    private void executeTest(String patternName) throws Exception {

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
