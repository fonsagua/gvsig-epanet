package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.DoubleValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.epanet.config.PipeFieldNames;
import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.network.IDCreator;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public class PipeLayer extends LinkLayer {

    private int diameterIdx;
    private int roughnessIdx;
    private int flowIdx;
    private int velocityIdx;
    private int unitHeadLossIdx;
    private int frictionFactorIdx;

    public PipeLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    protected LinkWrapper processSpecific(IFeature iFeature, NetworkBuilder nb) {
	NodeFinder nodeFinder = new NodeFinder(nb.getNodes(), nb.getAuxNodes());
	String id = IDCreator.addLink(iFeature.getID());
	PipeWrapper pipe = new PipeWrapper(iFeature);
	DoubleValue diameter = (DoubleValue) iFeature.getAttribute(diameterIdx);
	DoubleValue roughness = (DoubleValue) iFeature
		.getAttribute(roughnessIdx);

	Geometry geom = iFeature.getGeometry().toJTSGeometry();
	Coordinate[] coordinates = geom.getCoordinates();

	NodeWrapper startNode = nodeFinder
		.getNodeForInitialPoint(coordinates[0]);
	NodeWrapper endNode = nodeFinder.getNodeForEndPoint(coordinates[1]);

	pipe.createPipe(id, startNode, endNode, geom.getLength(),
		diameter.doubleValue(), roughness.doubleValue());
	nb.addPipe(pipe);
	return pipe;
    }

    @Override
    protected int[] getIndexes() {
	PipeFieldNames names = Preferences.getPipeFieldNames();
	SelectableDataSource recordset;
	try {
	    recordset = layer.getRecordset();
	    diameterIdx = recordset.getFieldIndexByName(names.getDiameter());
	    roughnessIdx = recordset.getFieldIndexByName(names.getRoughness());
	    flowIdx = recordset.getFieldIndexByName(names.getFlow());
	    velocityIdx = recordset.getFieldIndexByName(names.getVelocity());
	    unitHeadLossIdx = recordset.getFieldIndexByName(names
		    .getUnitHeadLoss());
	    frictionFactorIdx = recordset.getFieldIndexByName(names
		    .getFrictionFactor());

	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

	throwIfFieldNotFound(diameterIdx, names.getDiameter());
	throwIfFieldNotFound(roughnessIdx, names.getRoughness());
	throwIfFieldNotFound(flowIdx, names.getFlow());
	throwIfFieldNotFound(velocityIdx, names.getVelocity());
	throwIfFieldNotFound(unitHeadLossIdx, names.getUnitHeadLoss());
	throwIfFieldNotFound(frictionFactorIdx, names.getFrictionFactor());

	return new int[] { flowIdx, velocityIdx, unitHeadLossIdx,
		frictionFactorIdx };
    }

}
