package es.udc.cartolab.gvsig.epanet.structures.validations;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.exceptions.ErrorCode;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;

/**
 * Comprueba que todos los elementos de las capas de puntos est�n sobre un
 * v�rtice de la capa de tuber�as
 * 
 * En teor�a epanet deber�a realizar esta comprobaci�n, pero si se crea una
 * subred aislada, del tipo dos nodos con una bomba uni�ndolos se produce una
 * excepci�n en epanet
 */
public class IsolatedPoints implements LayerChecker {

    private static final Logger logger = Logger.getLogger(IsolatedPoints.class);

    private final List<Coordinate> coords = new ArrayList<Coordinate>();

    public IsolatedPoints() {
	View view = (View) PluginServices.getMDIManager().getActiveWindow();

	String pipeName = Preferences.getLayerNames().getPipes();
	FLyrVect pipes = (FLyrVect) view.getMapControl().getMapContext()
		.getLayers().getLayer(pipeName);
	ReadableVectorial pipesSrc = pipes.getSource();
	try {
	    for (int i = 0, max = pipesSrc.getShapeCount(); i < max; i++) {
		Geometry jts = pipesSrc.getShape(i).toJTSGeometry();
		for (Coordinate c : jts.getCoordinates()) {
		    coords.add(c);
		}
	    }
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    @Override
    public Warning check(FLyrVect layer) throws InvalidNetworkError {

	if (!Preferences.getPointLayers().contains(layer)) {
	    return null;
	}

	final ReadableVectorial layerSrc = layer.getSource();
	try {
        	for (int i = 0, max = layerSrc.getShapeCount(); i < max; i++) {
        	    IFeature feat = layerSrc.getFeature(i);
        	    Coordinate coord = feat.getGeometry().toJTSGeometry().getCoordinate();
        	    if (! coords.contains(coord)) {
        		throw new InvalidNetworkError(ErrorCode.ISOLATED_POINTS, String.format("Error de digitalizaci�n: El punto %s de la capa %s est� aislado o no est� correctamente conectado al sistema", feat.getID(), layer.getName()));
        	    }
        	}
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
	
	return null;
    }

}
