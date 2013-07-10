package es.udc.cartolab.gvsig.fonsagua.epanet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import es.udc.cartolab.gvsig.epanet.BaseformWrapper;
import es.udc.cartolab.gvsig.epanet.EpanetWrapper;

public class BaseformWrapperTest {

    private static EpanetWrapper epanet;

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @BeforeClass
    public static void setUpBeforeClass() {
	epanet = new BaseformWrapper("lib/BaseformEpaNetLib-1.0.jar");
    }

    @Test
    public void reportIsGenerated() throws IOException {
	final String inpPath = "fixtures/Net1.inp";
	final File copy = copyToTemp(inpPath);
	final String[] rptPath = epanet.execute(copy.getAbsolutePath());
	final File links = new File(rptPath[0]);
	final File nodes = new File(rptPath[1]);
	assertEquals(links.getName(), "Net1.inp.links.out");
	assertEquals(nodes.getName(), "Net1.inp.nodes.out");
	assertTrue(links.exists());
	assertTrue(nodes.exists());
    }

    private File copyToTemp(final String path) throws IOException {
	final File org = new File(path);
	FileUtils.copyFileToDirectory(org, temp.getRoot());
	return FileUtils.getFile(temp.getRoot(), org.getName());
    }

    @Test
    public void net1() throws IOException {
	final String inpPath = "fixtures/Net1.inp";
	final File copy = copyToTemp(inpPath);
	String actualRPT[] = epanet.execute(copy.getAbsolutePath());
	assertTrue(FileUtils.contentEquals(new File(actualRPT[0]), new File(
		"fixtures/Net1.inp.links.out")));
	assertTrue(FileUtils.contentEquals(new File(actualRPT[1]), new File(
		"fixtures/Net1.inp.nodes.out")));
    }

    @Test
    public void testNet1Fails() throws IOException {
	final String inpPath = "fixtures/Net1.inp";
	final File copy = copyToTemp(inpPath);
	String actualRPT[] = epanet.execute(copy.getAbsolutePath());
	assertFalse(FileUtils.contentEquals(new File(actualRPT[0]), new File(
		"fixtures/Net1.inp.nodes.out")));
	assertFalse(FileUtils.contentEquals(new File(actualRPT[1]), new File(
		"fixtures/Net1.inp.links.out")));
    }

    @Test
    public void net2() throws IOException {
	final String inpPath = "fixtures/Net2.inp";
	final File copy = copyToTemp(inpPath);
	String actualRPT[] = epanet.execute(copy.getAbsolutePath());
	assertTrue(FileUtils.contentEquals(new File(actualRPT[0]), new File(
		"fixtures/Net2.inp.links.out")));
	assertTrue(FileUtils.contentEquals(new File(actualRPT[1]), new File(
		"fixtures/Net2.inp.nodes.out")));
    }

    @Test
    public void testNet2Fails() throws IOException {
	final String inpPath = "fixtures/Net2.inp";
	final File copy = copyToTemp(inpPath);
	String actualRPT[] = epanet.execute(copy.getAbsolutePath());
	assertFalse(FileUtils.contentEquals(new File(actualRPT[0]), new File(
		"fixtures/Net1.inp.nodes.out")));
	assertFalse(FileUtils.contentEquals(new File(actualRPT[1]), new File(
		"fixtures/Net1.inp.links.out")));
    }

}
