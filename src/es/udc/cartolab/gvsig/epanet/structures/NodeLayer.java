package es.udc.cartolab.gvsig.epanet.structures;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

import es.udc.cartolab.gvsig.epanet.cad.ModifyValues;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public abstract class NodeLayer extends StructureLayer {

    private List<NodeWrapper> nodes;

    public NodeLayer(FLyrVect layer) {
	super(layer);
	nodes = new ArrayList<NodeWrapper>();
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
	NodeWrapper node = (NodeWrapper) processSpecific(iFeature, nb);
	Value[] attr = iFeature.getAttributes();

	node.setPressure(value2double(attr, 0));
	node.setHead(value2double(attr, 1));
	node.setDemand(value2double(attr, 2));

	return node;
    }

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

}
