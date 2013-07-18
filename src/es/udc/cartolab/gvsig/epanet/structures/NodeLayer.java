package es.udc.cartolab.gvsig.epanet.structures;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public abstract class NodeLayer {

    private List<NodeWrapper> nodes;
    protected FLyrVect layer;
    // Pressure / Head / Demand
    private int[] indexes;

    public NodeLayer(FLyrVect layer) {
	this.layer = layer;
	nodes = new ArrayList<NodeWrapper>();
	indexes = getIndexes();
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

    public void update() {
	ToggleEditing te = new ToggleEditing();
	te.startEditing(layer);
	for (int i = 0; i < nodes.size(); i++) {
	    NodeWrapper node = nodes.get(i);
	    String[] values = new String[] {
		    String.valueOf(node.getPressure()),
		    String.valueOf(node.getHead()),
		    String.valueOf(node.getDemand()) };

	    te.modifyValues(layer, i, indexes, values);
	}

	try {
	    te.stopEditing(layer, false);
	} catch (StopWriterVisitorException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    protected abstract int[] getIndexes();
}
