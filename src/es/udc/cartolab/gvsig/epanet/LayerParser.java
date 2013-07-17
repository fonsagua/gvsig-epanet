package es.udc.cartolab.gvsig.epanet;

import java.io.File;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.structures.JunctionLayer;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeLayer;
import es.udc.cartolab.gvsig.epanet.structures.PumpWrapper;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirLayer;
import es.udc.cartolab.gvsig.epanet.structures.StructureFactory;
import es.udc.cartolab.gvsig.epanet.structures.TankLayer;
import es.udc.cartolab.gvsig.epanet.structures.ValveWrapper;

public class LayerParser {

    private NetworkBuilder nb;
    private StructureFactory structureFactory;

    public LayerParser() {
	nb = new NetworkBuilder();
	IDCreator.reset();
	structureFactory = new StructureFactory(nb.getLinks(), nb.getNodes(),
		nb.getAuxNodes());
    }

    public void addJunctions(FLyrVect layer) {
	NodeLayer junctionLayer = new JunctionLayer(layer);
	junctionLayer.addToNetwork(nb);
    }

    public void addReservoirs(FLyrVect layer) {
	NodeLayer reservoirLayer = new ReservoirLayer(layer);
	reservoirLayer.addToNetwork(nb);
    }

    public void addTanks(FLyrVect layer) {
	NodeLayer tankLayer = new TankLayer(layer);
	tankLayer.addToNetwork(nb);
    }

    public void addPipes(FLyrVect layer) {
	ReadableVectorial readableVectorial = layer.getSource();

	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		LinkWrapper pipe = structureFactory.getPipe(iFeature);
		nb.addPipe(pipe);
	    }
	} catch (ExpansionFileReadException e) {
	    throw new ExternalError(e);
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}
    }

    public void addValves(FLyrVect layer) {
	ReadableVectorial readableVectorial = layer.getSource();

	// FIXME
	if (readableVectorial == null) {
	    return;
	}
	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		ValveWrapper valve = structureFactory.getFCV(iFeature);
		nb.addFlowControlValve(valve);
	    }
	} catch (ExpansionFileReadException e) {
	    throw new ExternalError(e);
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

    }

    public void addPumps(FLyrVect layer) {
	ReadableVectorial readableVectorial = layer.getSource();

	// FIXME
	if (readableVectorial == null) {
	    return;
	}
	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);

		PumpWrapper pump = structureFactory.getPump(iFeature);
		nb.addPump(pump);
	    }
	} catch (ExpansionFileReadException e) {
	    throw new ExternalError(e);
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}
    }

    public void createInpFile(File inp) {
	nb.createInpFile(inp);
    }
}
