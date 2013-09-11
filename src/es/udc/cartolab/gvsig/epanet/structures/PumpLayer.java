package es.udc.cartolab.gvsig.epanet.structures;

import java.util.List;
import java.util.Map;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.config.PumpFieldNames;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.math.MathUtils;
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
	NumericValue power = (NumericValue) iFeature.getAttribute(valueIdx);
	int baseDemand = 0;

	NodeWrapper startNode = getStartNode(nb, coordinate,
		elevation.doubleValue(), iFeature.getID());

	String endNodeId = IDCreator.addPumpNode(iFeature.getID());
	NodeWrapper endNode = new JunctionWrapper(endNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(endNodeId, endNode);

	String id = IDCreator.addPumpLink(iFeature.getID());
	// double power = Double.parseDouble(value.getValue());
	PumpWrapper pump = new PumpWrapper(iFeature);
	pump.createPump(id, startNode, endNode, power.doubleValue());
	nb.addPump(pump);
	return pump;
    }

    private NodeWrapper getStartNode(NetworkBuilder nb, Coordinate coordinate,
	    double elevation, String featureID) {
	NodeWrapper startNode = null;
	NodeFinder nodeFinder = new NodeFinder(nb.getNodes(), nb.getAuxNodes());
	List<NodeWrapper> existentNodesInThatCoord = nodeFinder
		.getNodesAt(coordinate);
	if (existentNodesInThatCoord.size() > 1) {
	    throw new InvalidNetworkError(
		    "Una bomba sólo puede estar sobre un tanque y en este punto hay más de un nodo");
	}

	if (existentNodesInThatCoord.size() == 1) {
	    NodeWrapper existentNodeInThatCoord = existentNodesInThatCoord
		    .get(0);
	    if (!(existentNodeInThatCoord instanceof TankWrapper)) {
		throw new InvalidNetworkError(
			"Una bomba sólo puede estar sobre un tanque y en este punto no hay un tanque");
	    }

	    if (!MathUtils.compare(elevation, existentNodeInThatCoord.getNode()
		    .getElevation())) {
		throw new InvalidNetworkError(
			"La elevación de la bomba y la del tanque sobre la que está no coinciden");
	    }

	    startNode = existentNodeInThatCoord;
	} else {
	    String startNodeId = IDCreator.addValveNode(featureID);
	    startNode = new JunctionWrapper(startNodeId, coordinate.x,
		    coordinate.y, elevation, 0);
	    nb.getAuxNodes().put(startNodeId, startNode);
	}

	return startNode;
    }

    @Override
    protected int[] getIndexes() {
	PumpFieldNames names = Preferences.getPumpFieldNames();

	try {
	    elevationIdx = getFieldIdx(names.getElevation());
	    valueIdx = getFieldIdx(names.getValue());
	    flowIdx = getFieldIdx(names.getFlow());
	    velocityIdx = getFieldIdx(names.getVelocity());
	    unitHeadLossIdx = getFieldIdx(names.getUnitHeadLoss());
	    frictionFactorIdx = getFieldIdx(names.getFrictionFactor());
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

	return new int[] { flowIdx, velocityIdx, unitHeadLossIdx,
		frictionFactorIdx };
    }

}
