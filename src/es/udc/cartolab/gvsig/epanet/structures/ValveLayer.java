package es.udc.cartolab.gvsig.epanet.structures;

import java.util.List;
import java.util.Map;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.config.ValveFieldNames;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.math.MathUtils;
import es.udc.cartolab.gvsig.epanet.network.IDCreator;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public class ValveLayer extends LinkLayer {

    private int elevationIdx;
    private int diameterIdx;
    private int settingIdx;
    private int flowIdx;
    private int velocityIdx;
    private int unitHeadLossIdx;
    private int frictionFactorIdx;

    public ValveLayer(FLyrVect layer) {
	super(layer);

    }

    @Override
    protected LinkWrapper processSpecific(IFeature iFeature, NetworkBuilder nb) {
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	NumericValue elevation = (NumericValue) iFeature
		.getAttribute(elevationIdx);
	NumericValue diameter = (NumericValue) iFeature
		.getAttribute(diameterIdx);
	NumericValue setting = (NumericValue) iFeature.getAttribute(settingIdx);

	Map<String, NodeWrapper> auxNodes = nb.getAuxNodes();

	NodeWrapper startNode = getStartNode(nb, coordinate,
		elevation.doubleValue(), iFeature.getID());

	String endNodeId = IDCreator.addValveNode(iFeature.getID());
	NodeWrapper endNode = new JunctionWrapper(endNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), 0);
	auxNodes.put(endNodeId, endNode);

	String id = IDCreator.addValveLink(iFeature.getID());
	ValveWrapper valve = new ValveWrapper(iFeature);
	valve.createValve(id, startNode, endNode, diameter.intValue(),
		setting.intValue());
	nb.addFlowControlValve(valve);
	return valve;
    }

    private NodeWrapper getStartNode(NetworkBuilder nb, Coordinate coordinate,
	    double elevation, String featureID) {
	NodeWrapper startNode = null;
	NodeFinder nodeFinder = new NodeFinder(nb.getNodes(), nb.getAuxNodes());
	List<NodeWrapper> existentNodesInThatCoord = nodeFinder
		.getNodesAt(coordinate);
	if (existentNodesInThatCoord.size() > 1) {
	    throw new InvalidNetworkError(
		    "Una v�lvula s�lo puede estar sobre una fuente y en este punto hay m�s de un nodo");
	}

	if (existentNodesInThatCoord.size() == 1) {
	    NodeWrapper existentNodeInThatCoord = existentNodesInThatCoord
		    .get(0);
	    if (!(existentNodeInThatCoord instanceof ReservoirWrapper)) {
		throw new InvalidNetworkError(
			"Una v�lvula s�lo puede estar sobre una fuente y en este punto no hay una fuente");
	    }

	    if (!MathUtils.compare(elevation, existentNodeInThatCoord.getNode()
		    .getElevation())) {
		throw new InvalidNetworkError(
			"La elevaci�n de la v�lvula y de la fuente sobre la que est� no coinciden");
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
	ValveFieldNames names = Preferences.getValveFieldNames();

	try {
	    elevationIdx = getFieldIdx(names.getElevation());
	    diameterIdx = getFieldIdx(names.getDiameter());
	    settingIdx = getFieldIdx(names.getSetting());
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
