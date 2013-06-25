package es.udc.cartolab.gvsig.fonsagua.epanet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestProperties {

    public static String epanetPath;
    static {
	Properties p = new Properties();
	FileInputStream in;
	try {
	    in = new FileInputStream("fixtures/test.properties");
	    p.load(in);
	    in.close();
	    epanetPath = p.getProperty("epanetPath");
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

}
