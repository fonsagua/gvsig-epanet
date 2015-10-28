package es.udc.cartolab.gvsig.epanet.structures;

import java.util.List;
import java.util.Map;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.config.PumpFieldNames;
import es.udc.cartolab.gvsig.epanet.exceptions.ErrorCode;
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
    protected LinkWrapper processSpecific(IFeature iFeature, NetworkBuilder nb)
	    throws InvalidNetworkError {
	Map<String, NodeWrapper> auxNodes = nb.getAuxNodes();
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	NumericValue elevation = getValue(iFeature, elevationIdx);
	NumericValue power = getValue(iFeature, valueIdx);
	int baseDemand = 0;

	NodeWrapper startNode = getStartNode(nb, coordinate,
		elevation.doubleValue(), iFeature.getID());

	String endNodeId = IDCreator.addPumpNode(iFeature.getID());
	NodeWrapper endNode = new JunctionWrapper(endNodeId, coordinate.x,
		coordinate.y, elevation.intValue(), baseDemand);
	auxNodes.put(endNodeId, endNode);

	String id = IDCreator.addPumpLink(iFeature.getID());
	PumpWrapper pump = new PumpWrapper(iFeature);
	pump.createPump(id, startNode, endNode, power.doubleValue());
	nb.addPump(pump);
	return pump;
    }

    private NodeWrapper getStartNode(NetworkBuilder nb, Coordinate coordinate,
	    double elevation, String featureID) throws InvalidNetworkError {
	NodeWrapper startNode = null;
	NodeFinder nodeFinder = new NodeFinder(nb.getNodes(), nb.getAuxNodes());
	List<NodeWrapper> existentNodesInThatCoord = nodeFinder
		.getNodesAt(coordinate);
	if (existentNodesInThatCoord.size() > 1) {
	    throw new InvalidNetworkError(ErrorCode.PUMP_POSITION,
		    "Una bomba s�lo puede estar sobre un tanque o pozo y en este punto hay m�s de un nodo");
	}

	if (existentNodesInThatCoord.size() == 1) {
	    NodeWrapper existentNodeInThatCoord = existentNodesInThatCoord
		    .get(0);
	    
	    if (!(existentNodeInThatCoord instanceof TankWrapper || isPozo(existentNodeInThatCoord))) {
		throw new InvalidNetworkError(ErrorCode.PUMP_POSITION,
			"Una bomba s�lo puede estar sobre un tanque o pozo y en este punto no hay un tanque");
	    }

	    if (!MathUtils.compare(elevation, existentNodeInThatCoord.getNode()
		    .getElevation())) {
		throw new InvalidNetworkError(ErrorCode.DATA_MISSMATCH,
			"La elevaci�n de la bomba y la del tanque o pozo sobre la que est� no coinciden");
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
    
    // FIXME. Este m�todo se introduce como workaround, pero est� rompiendo como
    // funciona gvsig-epanet. gvsig-epanet deber�a ser un plugin independiente,
    // cualquier conocimiento que tenga de las capas deber�a provenir del exterior
    private boolean isPozo(NodeWrapper existentNodeInThatCoord) {
	Value attribute = existentNodeInThatCoord.getFeature().getAttribute(4);
	return attribute.toString().equalsIgnoreCase("pozo");
    }

}
