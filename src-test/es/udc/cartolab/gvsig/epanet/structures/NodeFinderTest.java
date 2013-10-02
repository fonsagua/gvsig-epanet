package es.udc.cartolab.gvsig.epanet.structures;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;
import es.udc.cartolab.gvsig.epanet.structures.NodeFinder;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;

public class NodeFinderTest {

    @Test
    public void noNodesCanBeFoundInAnEmptyNetwork() throws InvalidNetworkError {
	NetworkBuilder nb = new NetworkBuilder();
	NodeFinder finder = new NodeFinder(nb.getNodes(), nb.getAuxNodes());
	final Coordinate coordinate = new Coordinate(1, 2);
	List<NodeWrapper> nodesAt = finder.getNodesAt(coordinate);

	assertEquals(nodesAt.size(), 0);

    }

    @Test(expected = InvalidNetworkError.class)
    public void exceptionIfNoNodesLookingForLineEndings()
	    throws InvalidNetworkError {
	NetworkBuilder nb = new NetworkBuilder();
	NodeFinder finder = new NodeFinder(nb.getNodes(), nb.getAuxNodes());
	final Coordinate coordinate = new Coordinate(1, 2);
	List<NodeWrapper> nodesAt = finder.getNodesAt(coordinate);

	finder.getNodeForEndPoint(coordinate);
	assertEquals(nodesAt.size(), 0);

	finder.getNodeForInitialPoint(coordinate);
	assertEquals(nodesAt.size(), 0);
    }
}
