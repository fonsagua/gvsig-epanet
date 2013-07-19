package es.udc.cartolab.gvsig.epanet.structures;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.DoubleValue;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public abstract class LinkLayer {

    private List<LinkWrapper> links;
    protected FLyrVect layer;
    private int[] indexes;

    public LinkLayer(FLyrVect layer) {
	this.layer = layer;
	links = new ArrayList<LinkWrapper>();
	indexes = getIndexes();
    }

    public void addToNetwork(NetworkBuilder nb) {
	ReadableVectorial readableVectorial = layer.getSource();

	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		LinkWrapper node = processSpecific(iFeature, nb);
		links.add(node);
	    }
	} catch (ExpansionFileReadException e) {
	    throw new ExternalError(e);
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}
    }

    public LinkWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	LinkWrapper link = processSpecific(iFeature, nb);
	Value[] attr = iFeature.getAttributes();

	double flow = ((DoubleValue) attr[indexes[0]]).doubleValue();
	link.setFlow(flow);

	double velocity = ((DoubleValue) attr[indexes[1]]).doubleValue();
	link.setVelocity(velocity);

	double uhloss = ((DoubleValue) attr[indexes[2]]).doubleValue();
	link.setUnitHeadLoss(uhloss);

	double ffactor = ((DoubleValue) attr[indexes[3]]).doubleValue();
	link.setFrictionFactor(ffactor);

	return link;
    }

    protected abstract LinkWrapper processSpecific(IFeature iFeature,
	    NetworkBuilder nb);

    public void update() {
	ToggleEditing te = new ToggleEditing();
	te.startEditing(layer);
	for (int i = 0; i < links.size(); i++) {
	    LinkWrapper link = links.get(i);
	    String[] values = new String[] { String.valueOf(link.getFlow()),
		    String.valueOf(link.getVelocity()),
		    String.valueOf(link.getUnitHeadLoss()),
		    String.valueOf(link.getFrictionFactor()) };

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

    protected void throwIfFieldNotFound(int index, String field) {
	if (index == -1) {
	    throw new ExternalError("Field not found: " + field + ", "
		    + layer.getName());
	}
    }

}
