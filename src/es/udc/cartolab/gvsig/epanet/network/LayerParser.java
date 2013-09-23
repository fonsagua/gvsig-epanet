package es.udc.cartolab.gvsig.epanet.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.exceptions.SimulationError;
import es.udc.cartolab.gvsig.epanet.math.MathUtils;
import es.udc.cartolab.gvsig.epanet.structures.JunctionLayer;
import es.udc.cartolab.gvsig.epanet.structures.JunctionWrapper;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PipeLayer;
import es.udc.cartolab.gvsig.epanet.structures.PumpLayer;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirLayer;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirWrapper;
import es.udc.cartolab.gvsig.epanet.structures.SourceLayer;
import es.udc.cartolab.gvsig.epanet.structures.TankLayer;
import es.udc.cartolab.gvsig.epanet.structures.ValveLayer;
import es.udc.cartolab.gvsig.epanet.structures.ValveWrapper;

public class LayerParser {

    private NetworkBuilder nb;
    private JunctionLayer junctionLayer;
    private ReservoirLayer reservoirLayer;
    private TankLayer tankLayer;
    private PipeLayer pipeLayer;
    private ValveLayer valveLayer;
    private PumpLayer pumpLayer;
    private SourceLayer sourceLayer;
    private List<String> simWarnings;

    public LayerParser() {
	nb = new NetworkBuilder();
	IDCreator.reset();
	simWarnings = new ArrayList<String>();
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

	addSources((FLyrVect) fLayers.getLayer(Preferences.getLayerNames()
		.getSources()));
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

	    checkDemandVsOffer();
	    updateLayers();
	} catch (IOException e) {
	    throw new ExternalError(e);
	} finally {
	    delete(inp);
	    delete(output);
	    delete(output);
	}

    }

    private void checkDemandVsOffer() {
	// TODO Auto-generated method stub

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
	    checkNegativePressure(node);
	    checkSourcePressure(node);
	    checkReservoirDemand(node);
	    simulated.remove(id);
	}
    }

    private void checkReservoirDemand(NodeWrapper node) {
	if (node instanceof ReservoirWrapper) {
	    if (node.getDemand() > 0) {
		simWarnings
			.add("Aviso de cálculo hidráulico: Algún embalse del sistema actúa como recpetor del agua y no como emisor");
	    }
	}
    }

    private void checkSourcePressure(NodeWrapper node) {
	// TODO: Not a really good form to check if the node is a Source
	if (node.getDemand() < 0) {
	    if (!MathUtils.inClosedInterval(-1, node.getPressure(), 0)) {
		simWarnings
			.add("Hay fuentes con presión cero o positiva, o muy negativa");
	    }
	}

    }

    private void checkNegativePressure(NodeWrapper node) {
	if (node instanceof JunctionWrapper) {
	    if (node.getDemand() > 0) {
		if (node.getPressure() < 0) {
		    simWarnings
			    .add("Aviso de cálculo hidráulico: Existen conexiones con demanda con presiones negativas");
		}
	    }
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
	    checkFlowSense(link);
	    checkValveLosts(link);
	    simulated.remove(id);
	}
    }

    private void checkValveLosts(LinkWrapper link) {
	if (link instanceof ValveWrapper) {
	    if (MathUtils.compare(link.getUnitHeadLoss(), 0)) {
		simWarnings
			.add("Aviso de cálculo hidráulico: La energía del sistema en área donde se ha ubicado una válvula no es sufieciente para aportar caudal");
	    }
	}

    }

    private void checkFlowSense(LinkWrapper link) {
	if (link.getFlow() < 0) {
	    simWarnings
		    .add("Aviso de cálculo hidráulico: Existen tuberías donde el agua discurre en sentido contrario al deseado");
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

    public List<String> getSimWarnings() {
	return simWarnings;
    }

}
