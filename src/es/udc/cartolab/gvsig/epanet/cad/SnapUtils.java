package es.udc.cartolab.gvsig.epanet.cad;

import java.util.ArrayList;
import java.util.Set;

import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;

public class SnapUtils {

    private ArrayList<FLayer> layersToSnap;

    public SnapUtils(Set<FLyrVect> snapeableLayers) {
	layersToSnap = new ArrayList<FLayer>();
	for (FLyrVect l : snapeableLayers) {
	    snapTo(l);
	}
    }

    public void enableSnappersFor(VectorialLayerEdited vle) {
	vle.setLayersToSnap(layersToSnap);
    }

    private void snapTo(FLyrVect l) {
	makeLayerSnapeable(l);
	layersToSnap.add(l);
    }

    private void makeLayerSnapeable(FLyrVect l) {
	if (!l.isEditing()) {
	    try {
		l.reload();
	    } catch (ReloadLayerException e) {
		throw new ExternalError(e);
	    }
	}
	l.setSpatialCacheEnabled(true);
    }

}
