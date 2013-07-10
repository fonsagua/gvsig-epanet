package es.udc.cartolab.gvsig.epanet;

import java.io.File;
import java.util.List;

import com.hardcode.gdbms.engine.values.DoubleValue;
import com.hardcode.gdbms.engine.values.IntValue;
import com.hardcode.gdbms.engine.values.StringValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;

public class LayerParser {

    private NetworkBuilder nb;
    private IDCreator idCreator;

    public LayerParser() {
	idCreator = new IDCreator();
	nb = new NetworkBuilder();
    }

    public void addPipes(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();

	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    String id = idCreator.addLink(iFeature.getID());
	    DoubleValue diameter = (DoubleValue) iFeature.getAttribute(0);
	    DoubleValue roughness = (DoubleValue) iFeature.getAttribute(1);

	    Geometry geom = iFeature.getGeometry().toJTSGeometry();
	    Coordinate[] coordinates = geom.getCoordinates();
	    String startNodeId = getNodeIdForInitialPoint(coordinates[0]);
	    String endNodeId = getNodeIdForEndPoint(coordinates[1]);

	    nb.getPipe(id, startNodeId, endNodeId, geom.getLength(),
		    diameter.intValue(), roughness.doubleValue());
	}
    }

    private String getNodeIdForEndPoint(Coordinate coordinate) {
	List<String> nodes = nb.getNodesIdsAt(coordinate);
	if (nodes.size() == 1) {
	    return nodes.get(0);
	} else if (nodes.size() == 2) {
	    int a = Integer.parseInt(nodes.get(0));
	    int b = Integer.parseInt(nodes.get(1));
	    int biggestID = a < b ? a : b;
	    return String.valueOf(biggestID);
	} else {
	    throw new InvalidNetworkError();
	}

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
    private String getNodeIdForInitialPoint(Coordinate coordinate) {
	List<String> nodes = nb.getNodesIdsAt(coordinate);
	if (nodes.size() == 1) {
	    return nodes.get(0);
	} else if (nodes.size() == 2) {
	    int a = Integer.parseInt(nodes.get(0));
	    int b = Integer.parseInt(nodes.get(1));
	    int biggestID = a > b ? a : b;
	    return String.valueOf(biggestID);
	} else {
	    throw new InvalidNetworkError();
	}

    }

    public void addJunctions(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();

	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    String id = idCreator.addNode(iFeature.getID());
	    Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		    .getCoordinate();
	    IntValue elevation = (IntValue) iFeature.getAttribute(0);
	    IntValue demand = (IntValue) iFeature.getAttribute(1);
	    nb.getNode(id, coordinate.x, coordinate.y, elevation.intValue(),
		    demand.intValue());
	}
    }

    public void addReservoirs(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();
	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    String id = idCreator.addNode(iFeature.getID());
	    Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		    .getCoordinate();
	    IntValue totalHead = (IntValue) iFeature.getAttribute(0);
	    nb.getReservoir(id, coordinate.x, coordinate.y,
		    totalHead.intValue());
	}
    }

    public void createInpFile(File inp) {
	nb.createInpFile(inp);
    }

    public void addTanks(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();

	// FIXME
	if (readableVectorial == null) {
	    return;
	}
	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    String id = idCreator.addNode(iFeature.getID());
	    Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		    .getCoordinate();
	    IntValue elevation = (IntValue) iFeature.getAttribute(0);
	    IntValue initLevel = (IntValue) iFeature.getAttribute(1);
	    IntValue minLevel = (IntValue) iFeature.getAttribute(2);
	    IntValue maxLevel = (IntValue) iFeature.getAttribute(3);
	    DoubleValue diameter = (DoubleValue) iFeature.getAttribute(4);

	    nb.getTank(id, coordinate.x, coordinate.y, elevation.intValue(),
		    initLevel.intValue(), minLevel.intValue(),
		    maxLevel.intValue(), diameter.doubleValue());
	}

    }

    public void addValves(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();

	// FIXME
	if (readableVectorial == null) {
	    return;
	}
	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		    .getCoordinate();
	    IntValue elevation = (IntValue) iFeature.getAttribute(0);
	    DoubleValue diameter = (DoubleValue) iFeature.getAttribute(1);
	    DoubleValue flow = (DoubleValue) iFeature.getAttribute(2);
	    int baseDemand = 0;

	    String startNode = idCreator.addValveNode(iFeature.getID());
	    nb.getNode(startNode, coordinate.x, coordinate.y,
		    elevation.intValue(), baseDemand);
	    String endNode = idCreator.addValveNode(iFeature.getID());
	    nb.getNode(endNode, coordinate.x, coordinate.y,
		    elevation.intValue(), baseDemand);

	    String id = idCreator.addValveLink(iFeature.getID());
	    nb.getFlowControlValve(id, startNode, endNode, diameter.intValue(),
		    flow.intValue());
	}

    }

    public void addPumps(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();

	// FIXME
	if (readableVectorial == null) {
	    return;
	}
	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		    .getCoordinate();
	    IntValue elevation = (IntValue) iFeature.getAttribute(0);
	    StringValue type = (StringValue) iFeature.getAttribute(1);
	    StringValue value = (StringValue) iFeature.getAttribute(2);
	    int baseDemand = 0;

	    String startNode = idCreator.addPumpNode(iFeature.getID());
	    nb.getNode(startNode, coordinate.x, coordinate.y,
		    elevation.intValue(), baseDemand);
	    String endNode = idCreator.addPumpNode(iFeature.getID());
	    nb.getNode(endNode, coordinate.x, coordinate.y,
		    elevation.intValue(), baseDemand);

	    String id = idCreator.addPumpLink(iFeature.getID());
	    double power = Double.parseDouble(value.getValue());
	    nb.getPumpWithPower(id, startNode, endNode, power);
	}
    }
}
