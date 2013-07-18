package es.udc.cartolab.gvsig.epanet.network;

import java.io.File;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.epanet.structures.JunctionLayer;
import es.udc.cartolab.gvsig.epanet.structures.PipeLayer;
import es.udc.cartolab.gvsig.epanet.structures.PumpLayer;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirLayer;
import es.udc.cartolab.gvsig.epanet.structures.TankLayer;
import es.udc.cartolab.gvsig.epanet.structures.ValveLayer;

public class LayerParser {

    private NetworkBuilder nb;
    private JunctionLayer junctionLayer;
    private ReservoirLayer reservoirLayer;
    private TankLayer tankLayer;
    private PipeLayer pipeLayer;
    private ValveLayer valveLayer;
    private PumpLayer pumpLayer;

    public LayerParser() {
	nb = new NetworkBuilder();
	IDCreator.reset();
    }

    public void addJunctions(FLyrVect layer) {
	junctionLayer = new JunctionLayer(layer);
	junctionLayer.addToNetwork(nb);
    }

    public void addReservoirs(FLyrVect layer) {
	reservoirLayer = new ReservoirLayer(layer);
	reservoirLayer.addToNetwork(nb);
    }

    public void addTanks(FLyrVect layer) {
	tankLayer = new TankLayer(layer);
	tankLayer.addToNetwork(nb);
    }

    public void addPipes(FLyrVect layer) {
	pipeLayer = new PipeLayer(layer);
	pipeLayer.addToNetwork(nb);
    }

    public void addValves(FLyrVect layer) {
	valveLayer = new ValveLayer(layer);
	valveLayer.addToNetwork(nb);
    }

    public void addPumps(FLyrVect layer) {
	pumpLayer = new PumpLayer(layer);
	pumpLayer.addToNetwork(nb);
    }

    public void createInpFile(File inp) {
	nb.createInpFile(inp);
    }

    public void hydraulicSim() {
	nb.hydraulicSim();
	junctionLayer.update();
    }
}
