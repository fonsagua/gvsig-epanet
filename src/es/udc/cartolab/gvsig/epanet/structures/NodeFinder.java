package es.udc.cartolab.gvsig.epanet.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;

public class NodeFinder {

    private final Map<String, NodeWrapper> nodes;
    private final Map<String, NodeWrapper> auxNodes;

    public NodeFinder(Map<String, NodeWrapper> nodes,
	    Map<String, NodeWrapper> auxNodes) {
	this.nodes = nodes;
	this.auxNodes = auxNodes;
    }

    public List<NodeWrapper> getNodessAt(Coordinate coordinate) {
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

	if ((finded.size() != 1) && (finded.size() != 2)) {
	    throw new InvalidNetworkError(
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
	List<NodeWrapper> overlapped = getNodessAt(coordinate);
	if (overlapped.size() == 1) {
	    return overlapped.get(0);
	} else if (overlapped.size() == 2) {
	    NodeWrapper startNode = overlapped.get(0);
	    int a = Integer.parseInt(startNode.getId());
	    NodeWrapper endNode = overlapped.get(1);
	    int b = Integer.parseInt(endNode.getId());
	    return a > b ? startNode : endNode;

	} else {
	    throw new InvalidNetworkError(
		    "More than two nodes in the same point");
	}

    }

    public NodeWrapper getNodeForEndPoint(Coordinate coordinate) {
	List<NodeWrapper> overlapped = getNodessAt(coordinate);
	if (overlapped.size() == 1) {
	    return overlapped.get(0);
	} else if (overlapped.size() == 2) {
	    NodeWrapper startNode = overlapped.get(0);
	    int a = Integer.parseInt(startNode.getId());
	    NodeWrapper endNode = overlapped.get(1);
	    int b = Integer.parseInt(endNode.getId());
	    return a < b ? startNode : endNode;

	} else {
	    throw new InvalidNetworkError(
		    "More than two nodes in the same point");
	}

    }
}
