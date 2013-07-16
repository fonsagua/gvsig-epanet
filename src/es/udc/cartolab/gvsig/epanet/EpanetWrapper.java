package es.udc.cartolab.gvsig.epanet;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;

public class EpanetWrapper {

    protected final String epanetPath;
    protected ParameterChecker checker;

    public EpanetWrapper(String epanetPath) {
	checker = new ParameterChecker();
	checker.throwIfFileNotReadable(epanetPath);
	this.epanetPath = epanetPath;
    }

    public String[] execute(String inp) {
	checker.throwIfFileNotReadable(inp);
	String rpt = FilenameUtils.removeExtension(inp) + ".rpt";
	String[] cmd = new String[] { epanetPath, inp, rpt };
	try {
	    Process process = Runtime.getRuntime().exec(cmd);
	    process.waitFor();
	} catch (IOException e) {
	    throw new ExternalError(e);
	} catch (InterruptedException e) {
	    throw new ExternalError(e);
	}
	return new String[] { rpt };
    }
}
