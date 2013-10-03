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

public abstract class LinkLayer extends StructureLayer {

    private List<LinkWrapper> links;

    public LinkLayer(FLyrVect layer) {
	super(layer);
	links = new ArrayList<LinkWrapper>();
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
	LinkWrapper link = (LinkWrapper) processSpecific(iFeature, nb);
	Value[] attr = iFeature.getAttributes();

	link.setFlow(value2double(attr, 0));
	link.setVelocity(value2double(attr, 1));
	link.setUnitHeadLoss(value2double(attr, 2));
	link.setFrictionFactor(value2double(attr, 3));

	return link;
    }

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

}
