package es.udc.cartolab.gvsig.epanet;

import java.io.File;

import com.hardcode.gdbms.engine.values.IntValue;
import com.hardcode.gdbms.engine.values.StringValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.StructureFactory;
import es.udc.cartolab.gvsig.epanet.structures.ValveWrapper;

public class LayerParser {

    private NetworkBuilder nb;
    private StructureFactory structureFactory;
    private IDCreator idCreator;

    public LayerParser() {
	nb = new NetworkBuilder();
	structureFactory = new StructureFactory(nb.getLinks(), nb.getNodes(),
		nb.getAuxNodes());
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
	    NodeWrapper node = structureFactory.getJunction(iFeature);
	    nb.addJunction(node);
	}
    }

    public void addReservoirs(FLyrVect layer) throws Exception {
	ReadableVectorial readableVectorial = layer.getSource();
	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    NodeWrapper reservoir = structureFactory.getReservoir(iFeature);
	    nb.addReservoir(reservoir);
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
	    NodeWrapper tank = structureFactory.getTank(iFeature);
	    nb.addTank(tank);
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
	    ValveWrapper valve = structureFactory.getFCV(iFeature);
	    nb.addFlowControlValve(valve);
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
