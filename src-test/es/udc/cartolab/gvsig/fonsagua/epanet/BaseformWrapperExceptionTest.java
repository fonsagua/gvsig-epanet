package es.udc.cartolab.gvsig.fonsagua.epanet;

import org.junit.BeforeClass;

public class BaseformWrapperExceptionTest extends EpanetWrapperExceptionTest {

    @BeforeClass
    public static void setUpBeforeClass() {
	epanet = new BaseformWrapper("binaries/BaseformEpaNetLib-1.0.jar");
    }
}
