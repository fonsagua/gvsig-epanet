package es.udc.cartolab.gvsig.epanet;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;

public class EpanetWrapper {

    protected final String epanetPath;

    public EpanetWrapper(String epanetPath) {
	throwIfFileNotReadable(epanetPath);
	this.epanetPath = epanetPath;
    }

    protected void throwIfFileNotReadable(String epanetPath) {
	if (fileIsNotReadable(epanetPath)) {
	    throw new IllegalArgumentException();
	}
    }

    protected boolean fileIsNotReadable(String path) {
	return (path == null) || (!new File(path).canRead());
    }

    public String[] execute(String inp) {
	throwIfFileNotReadable(inp);
	String rpt = FilenameUtils.removeExtension(inp) + ".rpt";
	String[] cmd = new String[] { epanetPath, inp, rpt };
	try {
	    Process tr = Runtime.getRuntime().exec(cmd);
	    tr.waitFor();
	} catch (IOException e) {
	    throw new ExternalError(e);
	} catch (InterruptedException e) {
	    throw new ExternalError(e);
	}
	return new String[] { rpt };
    }
}
