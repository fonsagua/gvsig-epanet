package es.udc.cartolab.gvsig.fonsagua.epanet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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
	FLyrVect layer = SHPFactory.getFLyrVectFromSHP(new File(temp.getRoot()
		.getAbsoluteFile() + File.separator + "junctions.shp"));
	layerParser.addJunctions(layer);
	layer = SHPFactory.getFLyrVectFromSHP(new File(temp.getRoot()
		.getAbsoluteFile() + File.separator + "reservoirs.shp"));
	layerParser.addReservoirs(layer);
	layer = SHPFactory.getFLyrVectFromSHP(new File(temp.getRoot()
		.getAbsoluteFile() + File.separator + "pipes.shp"));
	layerParser.addPipes(layer);

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
	assertFalse(FileUtils.contentEquals(new File(actualRPT2[0]), new File(
		"fixtures/" + patternName + ".inp.nodes.out")));
	assertFalse(FileUtils.contentEquals(new File(actualRPT2[1]), new File(
		"fixtures/" + patternName + ".inp.links.out")));
    }
}
