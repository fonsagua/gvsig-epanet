package es.udc.cartolab.gvsig.epanet;

import java.io.File;

import com.hardcode.gdbms.engine.values.DoubleValue;
import com.hardcode.gdbms.engine.values.IntValue;
import com.hardcode.gdbms.engine.values.StringValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.structures.JunctionWrapper;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeFinder;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirWrapper;
import es.udc.cartolab.gvsig.epanet.structures.StructureFactory;

public class LayerParser {

    private NetworkBuilder nb;
    private IDCreator idCreator;
    private NodeFinder nodeFinder;
    private StructureFactory structureFactory;

    public LayerParser() {
	idCreator = new IDCreator();
	nb = new NetworkBuilder();
	structureFactory = new StructureFactory(nb.getLinks(), nb.getNodes());
    }

    public void addPipes(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();

	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    LinkWrapper pipe = structureFactory.getPipe(iFeature);
	    nb.addPipe(pipe);
	}
    }

    public void addJunctions(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();

	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    NodeWrapper node = new JunctionWrapper(iFeature);
	    String id = idCreator.addNode(iFeature.getID());
	    nb.addJunction(id, node);
	}
    }

    public void addReservoirs(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();
	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    NodeWrapper reservoir = new ReservoirWrapper(iFeature);
	    String id = idCreator.addNode(iFeature.getID());
	    nb.addReservoir(id, reservoir);
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
	    nb.addJunction(startNode, coordinate.x, coordinate.y,
		    elevation.intValue(), baseDemand);
	    String endNode = idCreator.addValveNode(iFeature.getID());
	    nb.addJunction(endNode, coordinate.x, coordinate.y,
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
	    nb.addJunction(startNode, coordinate.x, coordinate.y,
		    elevation.intValue(), baseDemand);
	    String endNode = idCreator.addPumpNode(iFeature.getID());
	    nb.addJunction(endNode, coordinate.x, coordinate.y,
		    elevation.intValue(), baseDemand);

	    String id = idCreator.addPumpLink(iFeature.getID());
	    double power = Double.parseDouble(value.getValue());
	    nb.getPumpWithPower(id, startNode, endNode, power);
	}
    }
}
