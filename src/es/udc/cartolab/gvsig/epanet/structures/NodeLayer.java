package es.udc.cartolab.gvsig.epanet.structures;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

import es.udc.cartolab.gvsig.epanet.cad.ModifyValues;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

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

    public NodeWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	NodeWrapper node = processSpecific(iFeature, nb);
	Value[] attr = iFeature.getAttributes();

	double pressure = ((NumericValue) attr[indexes[0]]).doubleValue();
	node.setPressure(pressure);

	double head = ((NumericValue) attr[indexes[1]]).doubleValue();
	node.setHead(head);

	double demand = ((NumericValue) attr[indexes[2]]).doubleValue();
	node.setDemand(demand);

	return node;
    }

    protected abstract NodeWrapper processSpecific(IFeature iFeature,
	    NetworkBuilder nb);

    public void update() {

	ModifyValues te = new ModifyValues(layer);
	te.startEditing();

	for (int i = 0; i < nodes.size(); i++) {
	    NodeWrapper node = nodes.get(i);
	    Value[] values = new Value[] {
		    ValueFactory.createValue(node.getPressure()),
		    ValueFactory.createValue(node.getHead()),
		    ValueFactory.createValue(node.getDemand()) };
	    te.modifyValues(i, indexes, values);
	}

	te.stopEditing();

    }

    protected abstract int[] getIndexes();

    protected void throwIfFieldNotFound(int index, String field) {
	if (index == -1) {
	    throw new ExternalError("Field not found: " + field + ", "
		    + layer.getName());
	}
    }
}
