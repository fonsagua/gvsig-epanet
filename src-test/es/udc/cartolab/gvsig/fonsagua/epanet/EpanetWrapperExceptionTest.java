package es.udc.cartolab.gvsig.fonsagua.epanet;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.cartolab.gvsig.epanet.EpanetWrapper;
import es.udc.cartolab.gvsig.fonsagua.epanet.utils.TestProperties;

public class EpanetWrapperExceptionTest {
    protected static EpanetWrapper epanet;

    @BeforeClass
    public static void setUpBeforeClass() {
	epanet = new EpanetWrapper(TestProperties.epanetPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void epanetNotReadable() {
	new EpanetWrapper("not exists file");
    }

    @Test(expected = IllegalArgumentException.class)
    public void inpNotReadable() throws IOException {
	epanet.execute("fileNotExists");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullInpFile() throws IOException {
	epanet.execute(null);
    }
}
