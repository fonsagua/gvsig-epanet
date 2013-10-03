package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.gvsig.epanet.exceptions.ErrorCode;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public abstract class StructureLayer {

    protected FLyrVect layer;
    protected int[] indexes;

    public StructureLayer(FLyrVect layer) {
	this.layer = layer;
	indexes = getIndexes();
    }

    protected abstract int[] getIndexes();

    protected abstract StructureFeature processSpecific(IFeature iFeature,
	    NetworkBuilder nb) throws InvalidNetworkError;

    protected double value2double(Value[] attr, int i) {
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
