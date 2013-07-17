package es.udc.cartolab.gvsig.shputils;

import java.io.File;

import com.iver.cit.gvsig.fmap.layers.LayerFactory;

public class Drivers {

    public static void initgvSIGDrivers(String driversPath) {
    
        final File baseDriversPath = new File(driversPath);
        if (!baseDriversPath.exists()) {
            throw new RuntimeException("Can't find drivers path: "
        	    + driversPath);
        }
    
        LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
        if (LayerFactory.getDM().getDriverNames().length < 1) {
            throw new RuntimeException("Can't find drivers in path: "
        	    + driversPath);
        }
        LayerFactory.setWritersPath(baseDriversPath.getAbsolutePath());
        if (LayerFactory.getWM().getWriterNames().length < 1) {
            throw new RuntimeException("Can't find writers in path: "
        	    + driversPath);
        }
    }

}
