package es.udc.cartolab.gvsig.epanet.network;

import java.io.File;

public class ParameterChecker {

    public void throwIfFileNotReadable(String path) {
	if (fileIsNotReadable(path)) {
	    throw new IllegalArgumentException();
	}
    }

    private boolean fileIsNotReadable(String path) {
	return (path == null) || (!new File(path).canRead());
    }

}
