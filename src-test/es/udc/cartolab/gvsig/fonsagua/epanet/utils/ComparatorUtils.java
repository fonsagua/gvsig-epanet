package es.udc.cartolab.gvsig.fonsagua.epanet.utils;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.junit.rules.TemporaryFolder;

import com.hardcode.driverManager.DriverLoadException;

import es.udc.cartolab.gvsig.epanet.network.LayerParser;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;

public class ComparatorUtils {

    public static boolean rptComparator(File f1, File f2) throws Exception {

	FileInputStream fstream1 = new FileInputStream(f1);
	FileInputStream fstream2 = new FileInputStream(f2);

	DataInputStream in1 = new DataInputStream(fstream1);
	DataInputStream in2 = new DataInputStream(fstream2);

	BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
	BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));

	String strLine1, strLine2;
	br1.readLine();
	br2.readLine();
	while ((strLine1 = br1.readLine()) != null
		&& (strLine2 = br2.readLine()) != null) {
	    if (strLine1.contains("Analysis begun")
		    || strLine1.contains("Analysis ended")) {
		continue;
	    }
	    if (!strLine1.equals(strLine2)) {
		System.out.println(strLine1);
		System.out.println(strLine2);
		return false;
	    }
	}
	return true;
    }

    public static void checkNodes(NodeWrapper expected, NodeWrapper actual) {
	assertDoublesAreEqual(expected.getPressure(), actual.getPressure());
	assertDoublesAreEqual(expected.getHead(), actual.getHead());
	assertDoublesAreEqual(expected.getDemand(), actual.getDemand());
    }

    public static void checkLinks(LinkWrapper expected, LinkWrapper actual) {
	assertDoublesAreEqual(expected.getFlow(), actual.getFlow());
	assertDoublesAreEqual(expected.getVelocity(), actual.getVelocity());
	assertDoublesAreEqual(expected.getUnitHeadLoss(),
		actual.getUnitHeadLoss());
	assertDoublesAreEqual(expected.getFrictionFactor(),
		actual.getFrictionFactor());
    }

    public static Object[] parseSHPAfterSimulation(TemporaryFolder temp)
	    throws DriverLoadException, Exception {
	LayerParser lp = new LayerParser();
	FixtureLayerFactory flf = new FixtureLayerFactory(temp, lp);
	flf.parseSHPs();

	return new Object[] { lp.getNodes(), lp.getLinks() };
    }

    private static void assertDoublesAreEqual(double exp, double actual) {
	exp = round(exp);
	actual = round(actual);
	assertEquals(exp, actual, delta(exp));
    }

    public static double round(double value) {
	return Math.round(value * 100) / 100d;
    }

    public static double delta(double value) {
	return Math.abs(0.01 * value);
    }

}
