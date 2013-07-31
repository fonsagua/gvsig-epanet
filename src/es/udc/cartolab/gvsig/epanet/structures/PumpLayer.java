package es.udc.cartolab.gvsig.epanet.structures;

import java.util.Map;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.StringValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.config.PumpFieldNames;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.network.IDCreator;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public class PumpLayer extends LinkLayer {

    private int elevationIdx;
    private int valueIdx;
    private int flowIdx;
    private int velocityIdx;
    private int unitHeadLossIdx;
    private int frictionFactorIdx;

    public PumpLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    protected LinkWrapper processSpecific(IFeature iFeature, NetworkBuilder nb) {
	Map<String, NodeWrapper> auxNodes = nb.getAuxNodes();
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	NumericValue elevation = (NumericValue) iFeature
		.getAttribute(elevationIdx);
	// StringValue type = (StringValue) iFeature.getAttribute(1);
	StringValue value = (StringValue) iFeature.getAttribute(valueIdx);
	int baseDemand = 0;

	String startNodeId = IDCreator.addPumpNode(iFeature.getID());
	NodeWrapper startNode = new JunctionWrapper(startNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(startNodeId, startNode);

	String endNodeId = IDCreator.addPumpNode(iFeature.getID());
	NodeWrapper endNode = new JunctionWrapper(endNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(endNodeId, endNode);

	String id = IDCreator.addPumpLink(iFeature.getID());
	double power = Double.parseDouble(value.getValue());
	PumpWrapper pump = new PumpWrapper(iFeature);
	pump.createPump(id, startNode, endNode, power);
	nb.addPump(pump);
	return pump;
    }

    @Override
    protected int[] getIndexes() {
	PumpFieldNames names = Preferences.getPumpFieldNames();
	SelectableDataSource recordset;
	try {
	    recordset = layer.getRecordset();
	    elevationIdx = recordset.getFieldIndexByName(names.getElevation());
	    valueIdx = recordset.getFieldIndexByName(names.getValue());
	    flowIdx = recordset.getFieldIndexByName(names.getFlow());
	    velocityIdx = recordset.getFieldIndexByName(names.getVelocity());
	    unitHeadLossIdx = recordset.getFieldIndexByName(names
		    .getUnitHeadLoss());
	    frictionFactorIdx = recordset.getFieldIndexByName(names
		    .getFrictionFactor());

	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

	throwIfFieldNotFound(elevationIdx, names.getElevation());
	throwIfFieldNotFound(valueIdx, names.getValue());
	throwIfFieldNotFound(flowIdx, names.getFlow());
	throwIfFieldNotFound(velocityIdx, names.getVelocity());
	throwIfFieldNotFound(unitHeadLossIdx, names.getUnitHeadLoss());
	throwIfFieldNotFound(frictionFactorIdx, names.getFrictionFactor());

	return new int[] { flowIdx, velocityIdx, unitHeadLossIdx,
		frictionFactorIdx };
    }

}
