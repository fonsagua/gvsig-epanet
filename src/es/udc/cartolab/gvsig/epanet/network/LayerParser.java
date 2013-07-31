package es.udc.cartolab.gvsig.epanet.network;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.SimulationError;
import es.udc.cartolab.gvsig.epanet.structures.JunctionLayer;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
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

    /**
     * This is the preferred method to parse the layers that constitute a
     * network. Take into account that the order in which the layers are parse
     * is important: 1 - junctions 2 - reservoirs 3 - tanks 4 - valves 5 - pumps
     * 6 - pipes
     */
    public void add(FLayers fLayers) {

	addJunctions((FLyrVect) fLayers.getLayer(Preferences.getLayerNames()
		.getJunctions()));
	addReservoirs((FLyrVect) fLayers.getLayer(Preferences.getLayerNames()
		.getReservoirs()));
	addTanks((FLyrVect) fLayers.getLayer(Preferences.getLayerNames()
		.getTanks()));
	addValves((FLyrVect) fLayers.getLayer(Preferences.getLayerNames()
		.getValves()));
	addPumps((FLyrVect) fLayers.getLayer(Preferences.getLayerNames()
		.getPumps()));
	addPipes((FLyrVect) fLayers.getLayer(Preferences.getLayerNames()
		.getPipes()));
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

	File inp = null;
	String[] output = null;
	try {
	    inp = File.createTempFile("epanet", ".inp");

	    nb.createInpFile(inp);
	    BaseformWrapper epanet = new BaseformWrapper(
		    Preferences.getBaseformPath());
	    output = epanet.execute(inp.getAbsolutePath());
	    BaseformOuptutParser outputParser = new BaseformOuptutParser(
		    output[1], output[0]);

	    updateNodes(outputParser.getNodes());
	    updateAuxNodes(outputParser.getNodes());
	    updateLinks(outputParser.getLinks());

	    if (outputParser.getNodes().size() != 0) {
		throw new SimulationError(
			"El resultado del cálculo tiene más nodos de los esperados");
	    }
	    if (outputParser.getLinks().size() != 0) {
		throw new SimulationError(
			"El resultado del cálculo tiene más links de los esperados");
	    }
	    updateLayers();
	} catch (IOException e) {
	    throw new ExternalError(e);
	} finally {
	    delete(inp);
	    delete(output[0]);
	    delete(output[1]);
	}

    }

    private void delete(String path) {
	delete(new File(path));
    }

    private void delete(File file) {
	if ((file != null) && (file.exists())) {
	    file.delete();
	}
    }

    private void updateNodes(Map<String, NodeWrapper> simulated) {
	for (NodeWrapper node : nb.getNodes().values()) {
	    String id = node.getId();

	    NodeWrapper s = simulated.get(id);
	    if (s == null) {
		throw new SimulationError(
			"Un nodo de la red no está entre los resultados");
	    }
	    node.cloneResults(s);
	    simulated.remove(id);
	}
    }

    private void updateAuxNodes(Map<String, NodeWrapper> simulated) {
	for (NodeWrapper node : nb.getAuxNodes().values()) {
	    String id = node.getId();
	    NodeWrapper s = simulated.get(id);
	    if (s == null) {
		throw new SimulationError(
			"Un nodo auxiliar de la red no está entre los resultados");
	    }
	    node.cloneResults(s);
	    simulated.remove(id);
	}
    }

    private void updateLinks(Map<String, LinkWrapper> simulated) {
	for (LinkWrapper link : nb.getLinks().values()) {
	    String id = link.getId();
	    LinkWrapper s = simulated.get(id);
	    if (s == null) {
		throw new SimulationError(
			"Un link de la red no está entre los resultados");
	    }
	    link.cloneResults(s);
	    simulated.remove(id);
	}
    }

    private void updateLayers() {
	if (junctionLayer != null) {
	    junctionLayer.update();
	}
	if (tankLayer != null) {
	    tankLayer.update();
	}
	if (reservoirLayer != null) {
	    reservoirLayer.update();
	}
	if (pipeLayer != null) {
	    pipeLayer.update();
	}
	if (valveLayer != null) {
	    valveLayer.update();
	}
	if (pumpLayer != null) {
	    pumpLayer.update();
	}
    }

    public Map<String, NodeWrapper> getNodes() {
	return nb.getNodes();
    }

    public Map<String, LinkWrapper> getLinks() {
	return nb.getLinks();
    }

}
