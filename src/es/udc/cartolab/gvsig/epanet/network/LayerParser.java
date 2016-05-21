package es.udc.cartolab.gvsig.epanet.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.exceptions.SimulationError;
import es.udc.cartolab.gvsig.epanet.structures.JunctionLayer;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PipeLayer;
import es.udc.cartolab.gvsig.epanet.structures.PumpLayer;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirLayer;
import es.udc.cartolab.gvsig.epanet.structures.SourceLayer;
import es.udc.cartolab.gvsig.epanet.structures.TankLayer;
import es.udc.cartolab.gvsig.epanet.structures.ValveLayer;
import es.udc.cartolab.gvsig.epanet.structures.validations.LayerChecker;
import es.udc.cartolab.gvsig.epanet.structures.validations.LinkChecker;
import es.udc.cartolab.gvsig.epanet.structures.validations.NodeChecker;
import es.udc.cartolab.gvsig.epanet.structures.validations.NodesChecker;
import es.udc.cartolab.gvsig.epanet.structures.validations.Warning;

public class LayerParser {

    private NetworkBuilder nb;
    private JunctionLayer junctionLayer;
    private ReservoirLayer reservoirLayer;
    private TankLayer tankLayer;
    private PipeLayer pipeLayer;
    private ValveLayer valveLayer;
    private PumpLayer pumpLayer;
    private SourceLayer sourceLayer;
    private List<Warning> simWarnings;
    private Map<String, NodeChecker> nodeCheckers;
    private Map<String, NodesChecker> nodesCheckers;
    private Map<String, LinkChecker> linkCheckers;
    private Map<String, LayerChecker> layerCheckers;

    public LayerParser() {
	nb = new NetworkBuilder();
	IDCreator.reset();
	simWarnings = new ArrayList<Warning>();
	nodeCheckers = new HashMap<String, NodeChecker>();
	nodesCheckers = new HashMap<String, NodesChecker>();
	linkCheckers = new HashMap<String, LinkChecker>();
    }

    /**
     * This is the preferred method to parse the layers that constitute a
     * network. Take into account that the order in which the layers are parsed
     * is important: 1 sources - 2 junctions - 3 reservoirs - 4 tanks - 5 valves
     * - 6 pumps - 7 pipes
     * 
     * @throws InvalidNetworkError
     */
    public void add(FLayers fLayers) throws InvalidNetworkError {
	
	final FLyrVect sources = (FLyrVect) fLayers.getLayer(Preferences
		.getLayerNames().getSources());
	if (sources != null) {
	    checkLayer(sources);
	    addSources(sources);
	}
	final FLyrVect junctions = (FLyrVect) fLayers.getLayer(Preferences
		.getLayerNames().getJunctions());
	if (junctions != null) {
	    checkLayer(junctions);
	    addJunctions(junctions);
	}
	final FLyrVect reservoirs = (FLyrVect) fLayers.getLayer(Preferences
		.getLayerNames().getReservoirs());
	if (reservoirs != null) {
	    checkLayer(reservoirs);
	    addReservoirs(reservoirs);
	}

	final FLyrVect tanks = (FLyrVect) fLayers.getLayer(Preferences
		.getLayerNames().getTanks());
	if (tanks != null) {
	    checkLayer(tanks);
	    addTanks(tanks);
	}

	final FLyrVect valves = (FLyrVect) fLayers.getLayer(Preferences
		.getLayerNames().getValves());
	if (valves != null) {
	    checkLayer(valves);
	    addValves(valves);
	}

	final FLyrVect pumps = (FLyrVect) fLayers.getLayer(Preferences
		.getLayerNames().getPumps());
	if (pumps != null) {
	    checkLayer(pumps);
	    addPumps(pumps);
	}

	final FLyrVect pipes = (FLyrVect) fLayers.getLayer(Preferences
		.getLayerNames().getPipes());
	if (pipes != null) {
	    checkLayer(pipes);
	    addPipes(pipes);
	}
    }

    private void checkLayer(FLyrVect layer) throws InvalidNetworkError {
	for (LayerChecker checker : layerCheckers.values()) {
	    checker.check(layer);
	}
    }

    /**
     * A special version of a junction layer
     * 
     * @throws InvalidNetworkError
     */
    private void addSources(FLyrVect layer) throws InvalidNetworkError {
	sourceLayer = new SourceLayer(layer);
	sourceLayer.addToNetwork(nb);
    }

    public void addJunctions(FLyrVect layer) throws InvalidNetworkError {
	junctionLayer = new JunctionLayer(layer);
	junctionLayer.addToNetwork(nb);
    }

    public void addReservoirs(FLyrVect layer) throws InvalidNetworkError {
	reservoirLayer = new ReservoirLayer(layer);
	reservoirLayer.addToNetwork(nb);
    }

    public void addTanks(FLyrVect layer) throws InvalidNetworkError {
	tankLayer = new TankLayer(layer);
	tankLayer.addToNetwork(nb);
    }

    public void addPipes(FLyrVect layer) throws InvalidNetworkError {
	pipeLayer = new PipeLayer(layer);
	pipeLayer.addToNetwork(nb);
    }

    public void addValves(FLyrVect layer) throws InvalidNetworkError {
	valveLayer = new ValveLayer(layer);
	valveLayer.addToNetwork(nb);
    }

    public void addPumps(FLyrVect layer) throws InvalidNetworkError {
	pumpLayer = new PumpLayer(layer);
	pumpLayer.addToNetwork(nb);
    }

    public void createInpFile(File inp) throws InvalidNetworkError {
	nb.createInpFile(inp);
    }

    public void hydraulicSim() throws InvalidNetworkError {

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

	    for (NodesChecker checker : nodesCheckers.values()) {
		Warning warning = checker.check(nb.getNodes());

		if (warning != null) {
		    simWarnings.add(warning);
		}
	    }
	    updateLayers();
	} catch (IOException e) {
	    throw new ExternalError(e);
	} finally {
	    delete(inp);
	    delete(output);
	    delete(output);
	}

    }

    private void delete(String[] output) {
	if (output != null) {
	    for (String path : output) {
		delete(path);
	    }
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
	    for (NodeChecker checker : nodeCheckers.values()) {
		Warning warning = checker.check(node);
		if (warning != null) {
		    simWarnings.add(warning);
		}
	    }
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
	    for (LinkChecker checker : linkCheckers.values()) {
		Warning warning = checker.check(link);
		if (warning != null) {
		    simWarnings.add(warning);
		}
	    }
	    simulated.remove(id);
	}
    }

    private void updateLayers() {
	if (junctionLayer != null) {
	    junctionLayer.update();
	}
	if (sourceLayer != null) {
	    sourceLayer.update();
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

    public List<Warning> getSimWarnings() {
	return simWarnings;
    }

    public void setNodeCheckers(Map<String, NodeChecker> nodeCheckers) {
	this.nodeCheckers = nodeCheckers;
    }

    public void setNodesCheckers(Map<String, NodesChecker> nodesCheckers) {
	this.nodesCheckers = nodesCheckers;
    }

    public void setLinkCheckers(Map<String, LinkChecker> linkCheckers) {
	this.linkCheckers = linkCheckers;
    }

    public void setLayerCheckers(Map<String, LayerChecker> layerCheckers) {
	this.layerCheckers = layerCheckers;
    }

}
