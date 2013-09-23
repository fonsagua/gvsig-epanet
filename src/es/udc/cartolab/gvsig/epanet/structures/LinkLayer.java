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
import es.udc.cartolab.gvsig.epanet.exceptions.ErrorCode;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public abstract class LinkLayer {

    private List<LinkWrapper> links;
    protected FLyrVect layer;
    private int[] indexes;

    public LinkLayer(FLyrVect layer) {
	this.layer = layer;
	links = new ArrayList<LinkWrapper>();
	indexes = getIndexes();
    }

    public void addToNetwork(NetworkBuilder nb) throws InvalidNetworkError {
	ReadableVectorial readableVectorial = layer.getSource();

	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		LinkWrapper node = processFeature(iFeature, nb);
		links.add(node);
	    }
	} catch (ExpansionFileReadException e) {
	    throw new ExternalError(e);
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}
    }

    public LinkWrapper processFeature(IFeature iFeature, NetworkBuilder nb)
	    throws InvalidNetworkError {
	LinkWrapper link = processSpecific(iFeature, nb);
	Value[] attr = iFeature.getAttributes();

	link.setFlow(value2double(attr, 0));
	link.setVelocity(value2double(attr, 1));
	link.setUnitHeadLoss(value2double(attr, 2));
	link.setFrictionFactor(value2double(attr, 3));

	return link;
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
		ErrorCode.ILLEGAL_VALUE,
		"Error de introducción de datos: Existen elementos que no contienen todos los datos necesario para realizar los cálculo hidráulicos");
    }

    protected abstract LinkWrapper processSpecific(IFeature iFeature,
	    NetworkBuilder nb) throws InvalidNetworkError;

    public void update() {
	ModifyValues te = new ModifyValues(layer);
	te.startEditing();
	for (int i = 0; i < links.size(); i++) {
	    LinkWrapper link = links.get(i);
	    Value[] values = new Value[] {
		    ValueFactory.createValue(link.getFlow()),
		    ValueFactory.createValue(link.getVelocity()),
		    ValueFactory.createValue(link.getUnitHeadLoss()),
		    ValueFactory.createValue(link.getFrictionFactor()) };
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
