package es.udc.cartolab.gvsig.epanet.structures;

import java.util.Map;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.DoubleValue;
import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.config.ValveFieldNames;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.network.IDCreator;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public class ValveLayer extends LinkLayer {

    private int elevationIdx;
    private int diameterIdx;
    private int flowIdx;
    private int velocityIdx;
    private int unitHeadLossIdx;
    private int frictionFactorIdx;

    public ValveLayer(FLyrVect layer) {
	super(layer);

    }

    @Override
    public LinkWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	Map<String, NodeWrapper> auxNodes = nb.getAuxNodes();
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue elevation = (IntValue) iFeature.getAttribute(elevationIdx);
	DoubleValue diameter = (DoubleValue) iFeature.getAttribute(diameterIdx);
	DoubleValue flow = (DoubleValue) iFeature.getAttribute(flowIdx);
	int baseDemand = 0;

	String startNodeId = IDCreator.addValveNode(iFeature.getID());
	NodeWrapper startNode = new JunctionWrapper(startNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(startNodeId, startNode);

	String endNodeId = IDCreator.addValveNode(iFeature.getID());
	NodeWrapper endNode = new JunctionWrapper(endNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(endNodeId, endNode);

	String id = IDCreator.addValveLink(iFeature.getID());
	ValveWrapper valve = new ValveWrapper(iFeature);
	valve.createValve(id, startNode, endNode, diameter.intValue(),
		flow.intValue());
	nb.addFlowControlValve(valve);
	return valve;
    }

    @Override
    protected int[] getIndexes() {
	ValveFieldNames names = Preferences.getValveFieldNames();
	SelectableDataSource recordset;
	try {
	    recordset = layer.getRecordset();
	    elevationIdx = recordset.getFieldIndexByName(names.getElevation());
	    diameterIdx = recordset.getFieldIndexByName(names.getDiameter());
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
	throwIfFieldNotFound(diameterIdx, names.getDiameter());
	throwIfFieldNotFound(flowIdx, names.getFlow());
	throwIfFieldNotFound(velocityIdx, names.getVelocity());
	throwIfFieldNotFound(unitHeadLossIdx, names.getUnitHeadLoss());
	throwIfFieldNotFound(frictionFactorIdx, names.getFrictionFactor());

	return new int[] { flowIdx, velocityIdx, unitHeadLossIdx,
		frictionFactorIdx };
    }
}
