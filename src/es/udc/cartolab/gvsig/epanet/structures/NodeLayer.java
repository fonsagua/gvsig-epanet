package es.udc.cartolab.gvsig.epanet.structures;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

import es.udc.cartolab.gvsig.epanet.NetworkBuilder;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;

public abstract class NodeLayer {

    private List<NodeWrapper> nodes;
    private FLyrVect layer;

    public NodeLayer(FLyrVect layer) {
	this.layer = layer;
	nodes = new ArrayList<NodeWrapper>();
    }

    public void addToNetwork(NetworkBuilder nb) {
	ReadableVectorial readableVectorial = layer.getSource();

	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		NodeWrapper node = processFeature(iFeature, nb);
		nodes.add(node);
	    }
	} catch (ExpansionFileReadException e) {
	    throw new ExternalError(e);
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}
    }

    public abstract NodeWrapper processFeature(IFeature iFeature,
	    NetworkBuilder nb);

}
