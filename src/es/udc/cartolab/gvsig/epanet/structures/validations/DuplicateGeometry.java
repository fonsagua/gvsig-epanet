package es.udc.cartolab.gvsig.epanet.structures.validations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.epanet.exceptions.ErrorCode;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;

public class DuplicateGeometry implements LayerChecker {
    // TODO. En lugar de lanzar el error en cuanto hay un elemento duplicado
    // sería mejor buscar todos los elementos duplicados y después avisar
    
    @Override
    public Warning check(FLyrVect layer) throws InvalidNetworkError {
	ReadableVectorial readableVectorial = layer.getSource();
	List<Geometry> geoms = new ArrayList<Geometry>();
	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		
		Geometry geom = iFeature.getGeometry().toJTSGeometry();
		for (Geometry g : geoms) {
		    if (geom.equals(g)) {
			throw new InvalidNetworkError(ErrorCode.DUPLICATED_GEOMETRY, String.format("Elemento %s duplicado en capa %s", iFeature.getID(), layer.getName()));
		    }
		}
		geoms.add(geom);
	    }
	} catch (ExpansionFileReadException e) {
	    throw new ExternalError(e);
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}
	return null;
    }
}