package es.udc.cartolab.gvsig.fonsagua.epanet;

import org.junit.BeforeClass;

import es.udc.cartolab.gvsig.epanet.network.BaseformWrapper;

public class BaseformWrapperExceptionTest extends EpanetWrapperExceptionTest {

    @BeforeClass
    public static void setUpBeforeClass() {
	epanet = new BaseformWrapper("lib/BaseformEpaNetLib-1.0.jar");
    }
}
