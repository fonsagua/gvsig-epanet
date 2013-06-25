package es.udc.cartolab.gvsig.fonsagua.epanet;

import java.io.IOException;

import es.udc.cartolab.gvsig.fonsagua.epanet.exceptions.ExternalError;

public class BaseformWrapper extends EpanetWrapper {

    public BaseformWrapper(String epanetPath) {
	super(epanetPath);
    }

    @Override
    public String[] execute(String inpPath) {

	if (fileIsNotReadable(inpPath)) {
	    throw new IllegalArgumentException();
	}

	String[] baseform = new String[] { "java", "-cp", epanetPath,
		"org.addition.epanet.EPATool", "", inpPath };
	try {
	    Process tr = Runtime.getRuntime().exec(baseform);
	    tr.waitFor();
	} catch (IOException e) {
	    throw new ExternalError(e);
	} catch (InterruptedException e) {
	    throw new ExternalError(e);
	}
	return new String[] { inpPath + ".links.out", inpPath + ".nodes.out" };
    }

}
