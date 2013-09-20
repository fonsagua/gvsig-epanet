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
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.gvsig.epanet.cad.ModifyValues;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
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

    public void addToNetwork(NetworkBuilder nb) throws InvalidNetworkError {
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

    public NodeWrapper processFeature(IFeature iFeature, NetworkBuilder nb)
	    throws InvalidNetworkError {
	NodeWrapper node = processSpecific(iFeature, nb);
	Value[] attr = iFeature.getAttributes();

	node.setPressure(value2double(attr, 0));
	node.setHead(value2double(attr, 1));
	node.setDemand(value2double(attr, 2));

	return node;
    }

    private double value2double(Value[] attr, int i) {
	double r = 0;
	if (indexes[i] != -1) {
	    Value value = attr[indexes[i]];
	    if (value instanceof NumericValue) {
		r = ((NumericValue) value).doubleValue();
	    }
	}
	return r;
    }

    protected NumericValue getValue(IFeature iFeature, int idx)
	    throws InvalidNetworkError {
	Value attribute = iFeature.getAttribute(idx);
	if (attribute instanceof NumericValue) {
	    return (NumericValue) attribute;
	}
	throw new InvalidNetworkError(
		202,
		"Error de introducción de datos: Existen elementos que no contienen todos los datos necesario para realizar los cálculo hidráulicos");
    }

    protected abstract NodeWrapper processSpecific(IFeature iFeature,
	    NetworkBuilder nb) throws InvalidNetworkError;

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

    protected int getFieldIdx(String name) throws ReadDriverException {
	int idx = -1;
	if (name != null) {
	    SelectableDataSource recordset = layer.getRecordset();
	    idx = recordset.getFieldIndexByName(name);
	    throwIfFieldNotFound(idx, name);
	}
	return idx;
    }

    protected void throwIfFieldNotFound(int index, String field) {
	if (index == -1) {
	    throw new ExternalError("Field not found: " + field + ", "
		    + layer.getName());
	}
    }
}
