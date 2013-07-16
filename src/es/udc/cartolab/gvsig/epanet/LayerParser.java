package es.udc.cartolab.gvsig.epanet;

import java.io.File;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PumpWrapper;
import es.udc.cartolab.gvsig.epanet.structures.StructureFactory;
import es.udc.cartolab.gvsig.epanet.structures.ValveWrapper;

public class LayerParser {

    private NetworkBuilder nb;
    private StructureFactory structureFactory;

    public LayerParser() {
	nb = new NetworkBuilder();
	structureFactory = new StructureFactory(nb.getLinks(), nb.getNodes(),
		nb.getAuxNodes());
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

    public void addJunctions(FLyrVect layer) {
	ReadableVectorial readableVectorial = layer.getSource();

	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		NodeWrapper node = structureFactory.getJunction(iFeature);
		nb.addJunction(node);
	    }
	} catch (ExpansionFileReadException e) {
	    throw new ExternalError(e);
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}
    }

    public void addReservoirs(FLyrVect layer) {
	ReadableVectorial readableVectorial = layer.getSource();
	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		NodeWrapper reservoir = structureFactory.getReservoir(iFeature);
		nb.addReservoir(reservoir);
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

    public void addTanks(FLyrVect layer) {
	ReadableVectorial readableVectorial = layer.getSource();

	// FIXME
	if (readableVectorial == null) {
	    return;
	}
	try {
	    for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
		IFeature iFeature = readableVectorial.getFeature(i);
		NodeWrapper tank = structureFactory.getTank(iFeature);
		nb.addTank(tank);
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
}
