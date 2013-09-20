package es.udc.cartolab.gvsig.epanet.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkErrorToFix;
import es.udc.cartolab.gvsig.epanet.math.MathUtils;

public class NodeFinder {

    private final Map<String, NodeWrapper> nodes;
    private final Map<String, NodeWrapper> auxNodes;

    public NodeFinder(Map<String, NodeWrapper> nodes,
	    Map<String, NodeWrapper> auxNodes) {
	this.nodes = nodes;
	this.auxNodes = auxNodes;
    }

    public List<NodeWrapper> getNodesAt(Coordinate coordinate) {
	List<NodeWrapper> finded = new ArrayList<NodeWrapper>(1);

	for (NodeWrapper n : nodes.values()) {
	    if (coordinate.x == n.getX() && coordinate.y == n.getY()) {
		finded.add(n);
	    }
	}
	for (NodeWrapper n : auxNodes.values()) {
	    if (coordinate.x == n.getX() && coordinate.y == n.getY()) {
		finded.add(n);
	    }
	}

	if (!MathUtils.inClosedInterval(0, finded.size(), 2)) {
	    throw new InvalidNetworkErrorToFix(
		    "More than two nodes in the same point");
	}

	return finded;
    }

    /*
     * Cuando el extremo de la tubería sea una válvula, no habrá un sólo nodo,
     * si no que habrá dos superpuestos. Si, se trata del extremo inicial de la
     * tubería tendré que escoger el nodo con el mayor id (una feature del shp
     * de válvulas, crea automáticamente dos nodos de id's consecutivos). Si se
     * trata del extremo final de la tubería tendré que escoger el que tenga
     * menor id Esto asume que las tuberías se digitalizan en el sentido del
     * agua
     */
    public NodeWrapper getNodeForInitialPoint(Coordinate coordinate) {
	List<NodeWrapper> overlapped = getNodesAt(coordinate);
	if (overlapped.size() == 1) {
	    return overlapped.get(0);
	} else if (overlapped.size() == 2) {
	    NodeWrapper startNode = overlapped.get(0);
	    int a = Integer.parseInt(startNode.getId());
	    NodeWrapper endNode = overlapped.get(1);
	    int b = Integer.parseInt(endNode.getId());
	    return a > b ? startNode : endNode;

	} else if (overlapped.size() == 0) {
	    throw new InvalidNetworkErrorToFix(
		    "Error de digitalización: Existen tuberías aisladas o no conectadas al sistema. El punto INICIAL de la tubería no está conectado a ningún nodo");
	} else {
	    throw new InvalidNetworkErrorToFix(overlapped.size()
		    + " nodes at this point. Should be 1 or 2.");
	}

    }

    public NodeWrapper getNodeForEndPoint(Coordinate coordinate) {
	List<NodeWrapper> overlapped = getNodesAt(coordinate);
	if (overlapped.size() == 1) {
	    return overlapped.get(0);
	} else if (overlapped.size() == 2) {
	    NodeWrapper startNode = overlapped.get(0);
	    int a = Integer.parseInt(startNode.getId());
	    NodeWrapper endNode = overlapped.get(1);
	    int b = Integer.parseInt(endNode.getId());
	    return a < b ? startNode : endNode;

	} else if (overlapped.size() == 0) {
	    throw new InvalidNetworkErrorToFix(
		    "Error de digitalización: Existen tuberías aisladas o no conectadas al sistema. El punto FINAL de la tubería no está conectado a ningún nodo");
	} else {
	    throw new InvalidNetworkErrorToFix(overlapped.size()
		    + " nodes at this point. Should be 1 or 2.");
	}

    }
}
