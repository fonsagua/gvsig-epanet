package es.udc.cartolab.gvsig.epanet;

import java.io.IOException;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;

public class BaseformWrapper extends EpanetWrapper {

    public BaseformWrapper(String epanetPath) {
	super(epanetPath);
    }

    @Override
    public String[] execute(String inpPath) {
	checker.throwIfFileNotReadable(inpPath);

	String[] baseform = new String[] { "java", "-cp", epanetPath,
		"org.addition.epanet.EPATool", "", inpPath };
	try {
	    Process process = Runtime.getRuntime().exec(baseform);
	    process.waitFor();
	} catch (IOException e) {
	    throw new ExternalError(e);
	} catch (InterruptedException e) {
	    throw new ExternalError(e);
	}
	return new String[] { inpPath + ".links.out", inpPath + ".nodes.out" };
    }

}
