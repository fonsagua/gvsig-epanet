package es.udc.cartolab.gvsig.epanet;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.addition.epanet.hydraulic.HydraulicSim;
import org.addition.epanet.hydraulic.io.AwareStep;
import org.addition.epanet.hydraulic.io.HydraulicReader;
import org.addition.epanet.network.FieldsMap;
import org.addition.epanet.network.Network;
import org.addition.epanet.network.Network.FileType;
import org.addition.epanet.network.PropertiesMap;
import org.addition.epanet.network.PropertiesMap.FlowUnitsType;
import org.addition.epanet.network.PropertiesMap.FormType;
import org.addition.epanet.network.PropertiesMap.ReportFlag;
import org.addition.epanet.network.PropertiesMap.StatFlag;
import org.addition.epanet.network.io.input.InputParser;
import org.addition.epanet.network.io.output.OutputComposer;
import org.addition.epanet.network.structures.Control;
import org.addition.epanet.network.structures.Control.ControlType;
import org.addition.epanet.network.structures.Curve;
import org.addition.epanet.network.structures.Curve.CurveType;
import org.addition.epanet.network.structures.Link;
import org.addition.epanet.network.structures.Link.LinkType;
import org.addition.epanet.network.structures.Link.StatType;
import org.addition.epanet.network.structures.Node;
import org.addition.epanet.network.structures.Pattern;
import org.addition.epanet.network.structures.Point;
import org.addition.epanet.network.structures.Pump;
import org.addition.epanet.network.structures.Tank;
import org.addition.epanet.network.structures.Valve;
import org.addition.epanet.util.ENException;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.structures.JunctionWrapper;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PipeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirWrapper;

//Los getXX pueden seguir devolviendo el valor para poder referenciar
//el objeto en lugar del ID
//
//Hay que conseguir una instancia de NullParser y llamar a convert
public class NetworkBuilder {

    private Network net;
    private InputParser parser;
    private static Logger log;

    private final Map<String, NodeWrapper> nodesWrapper;
    private final Map<String, LinkWrapper> linksWrapper;

    public NetworkBuilder() {
	nodesWrapper = new HashMap<String, NodeWrapper>();
	linksWrapper = new HashMap<String, LinkWrapper>();

	log = Logger.getLogger(this.getClass().getName());
	parser = InputParser.create(FileType.NULL_FILE,
		Logger.getLogger(getClass().getName()));

	net = new Network();

	try {
	    // Initialize Units is mandatory
	    net.updatedUnitsProperty();
	} catch (ENException e1) {
	    e1.printStackTrace();
	}
	setEnergyOptions();
	setReactionsOptions();
	setTimesOptions();
	setReportOptions();
	setOptions();

    }

    private void setOptions() {
	PropertiesMap pMap = net.getPropertiesMap();
	try {
	    pMap.setFlowflag(FlowUnitsType.LPS);
	    pMap.setFormflag(FormType.DW); // Headloss Formula
	    // TODO: A lot of options
	} catch (ENException e) {
	    e.printStackTrace();
	}
    }

    private void setReportOptions() {
	try {
	    net.getPropertiesMap().setStatflag(StatFlag.FALSE);
	    net.getPropertiesMap().setSummaryflag(false);
	    net.getPropertiesMap().setLinkflag(ReportFlag.TRUE);
	    net.getPropertiesMap().setNodeflag(ReportFlag.TRUE);
	    net.getPropertiesMap().setPageSize(0);
	} catch (ENException e) {
	    e.printStackTrace();
	}

    }

    private void setTimesOptions() {
	// try {
	// pMap.setTstatflag(TstatType.SERIES);
	// pMap.setDuration(3600 * 24);
	// pMap.setHstep(hstep)
	//
	// } catch (ENException e) {
	// e.printStackTrace();
	// }
    }

    private void setReactionsOptions() {
	try {
	    net.getPropertiesMap().setBulkOrder(1.0);
	    net.getPropertiesMap().setTankOrder(1.0);
	    net.getPropertiesMap().setWallOrder(1.0);
	    net.getPropertiesMap().setRfactor(0.0); // Roughness Correlation
	    // TODO: Global Bulk; Global Wall; Limiting Potential
	} catch (ENException e1) {
	    e1.printStackTrace();
	}
    }

    private void setEnergyOptions() {
	// ENERGY
	try {
	    net.getPropertiesMap().setEpump(75.0); // Global Efficiency
	    net.getPropertiesMap().setEcost(0.0); // Global Price
	    net.getPropertiesMap().setDcost(0.0); // Demand Charge
	} catch (ENException e1) {
	    e1.printStackTrace();
	}
    }

    public Network getNet() {
	return net;
    }

    public void addJunction(String id, double x, double y, int elevation,
	    int baseDemand) {
	NodeWrapper node = new JunctionWrapper(id, x, y, elevation, baseDemand);
	addJunction(id, node);
    }

    public void addJunction(String id, NodeWrapper node) {
	nodesWrapper.put(id, node);
	net.addJunction(id, node.getNode());
    }

    public void getTank(String id, double x, double y, int elevation,
	    int initLevel, int minLevel, int maxLevel, double diameter) {
	// TODO: MinVol
	Tank tank = new Tank();
	tank.setId(id);
	tank.setPosition(new Point(x, y));
	tank.setElevation(elevation);
	tank.setH0(initLevel);
	tank.setHmin(minLevel);
	tank.setHmax(maxLevel);
	tank.setArea(diameter);
	tank.setMixModel(Tank.MixType.MIX1); // deber�a estar en initTanks
	net.addTank(tank.getId(), tank);

    }

    public void addReservoir(String id, double x, double y, int totalHead) {
	NodeWrapper reservoir = new ReservoirWrapper(id, x, y, totalHead);
	addReservoir(id, reservoir);
    }

    public void addReservoir(String id, NodeWrapper reservoir) {
	nodesWrapper.put(id, reservoir);
	net.addTank(id, (Tank) reservoir.getNode());
    }

    /**
     * This method should be avoided. It makes that a new constructor in
     * pipewrapper is needed We should use a factory, or at least remove the
     * pipewrapper constructor
     */
    public void addPipe(String id, String startNode, String endNode,
	    double len, double diameter, double roughness) {

	LinkWrapper pipe = new PipeWrapper(id, nodesWrapper.get(startNode),
		nodesWrapper.get(endNode), len, diameter, roughness);
	addPipe(id, pipe);
    }

    public void addPipe(String id, LinkWrapper pipe) {
	linksWrapper.put(id, pipe);
	net.addPipe(id, pipe.getLink());
    }

    public void addPipe(LinkWrapper pipe) {
	net.addPipe(pipe.getId(), pipe.getLink());
    }

    private Pump getPump(String id, String startNode, String endNode) {
	Pump pump = new Pump();
	pump.setId(id);
	// Link attributes
	pump.setFirst(net.getNode(startNode));
	pump.setSecond(net.getNode(endNode));
	pump.setDiameter(0);
	pump.setLenght(0.0d);
	pump.setRoughness(1.0d);
	pump.setKm(0.0d);
	pump.setKb(0.0d);
	pump.setKw(0.0d);
	pump.setType(LinkType.PUMP);
	pump.setStatus(StatType.OPEN);
	pump.setReportFlag(false);

	// Pump attributes
	pump.setHcurve(null);
	pump.setEcurve(null);
	pump.setUpat(null);
	pump.setEcost(0.0d);
	pump.setEpat(null);
	return pump;
    }

    public void getPumpWithPower(String id, String startNode, String endNode,
	    double power) {

	Pump pump = getPump(id, startNode, endNode);
	pump.setPtype(Pump.Type.CONST_HP);
	pump.setKm(power);
	net.addPump(pump.getId(), pump);
    }

    public void getPumpWithCurve(String id, String startNode, String endNode,
	    String headCurveId) {
	Pump pump = getPump(id, startNode, endNode);
	pump.setPtype(Pump.Type.NOCURVE);
	pump.setHcurve(net.getCurve(headCurveId));
	net.addPump(pump.getId(), pump);
    }

    public void getHeadCurve(String id, CurveType type, double flow, double head) {
	Curve curve = new Curve();
	curve.setId(id);
	curve.setType(type);
	curve.getX().add(flow);
	curve.getY().add(head);
	net.addCurve(curve.getId(), curve);
    }

    public Pattern getPattern(String id, double[] multipliers) {
	Pattern pattern = new Pattern();
	pattern.setId(id);
	for (int i = 0; i < multipliers.length; i++) {
	    pattern.add(multipliers[i]);
	}

	return pattern;
    }

    public void getPumpControl(String link, String node, ControlType type,
	    StatType status, double level) {
	Control control = new Control();
	control.setLink(net.getLink(link));
	control.setNode(net.getNode(node));
	control.setType(type);
	control.setStatus(status);
	control.setSetting(1.0);
	control.setGrade(level);
	control.setTime(0);
	net.addControl(control);
    }

    public void getFlowControlValve(String id, String startNode,
	    String endNode, double diameter, double flow) {
	// TODO: MinorLoss; Checks from InpParser
	Valve valve = new Valve();
	valve.setId(id);
	valve.setFirst(net.getNode(startNode));
	valve.setSecond(net.getNode(endNode));
	valve.setDiameter(diameter);
	valve.setLenght(0.0d);
	valve.setRoughness(flow);
	valve.setKm(0.0);
	valve.setKb(0.0d);
	valve.setKw(0.0d);
	valve.setType(LinkType.FCV);
	valve.setStatus(StatType.ACTIVE);
	net.addValve(valve.getId(), valve);

    }

    private void prepare() {
	// TODO: Como gestionar esto
	try {
	    parser.parse(net, null);
	} catch (ENException e) {
	    throw new InvalidNetworkError();
	}
    }

    public void createInpFile(File ofile) {
	prepare();
	OutputComposer composer = OutputComposer.create(FileType.INP_FILE);
	try {
	    composer.composer(net, ofile);
	} catch (ENException e) {
	    throw new InvalidNetworkError();
	}
    }

    public void hydraulicSim() {
	prepare();
	try {
	    File hydFile = File.createTempFile("hydSim", "bin");
	    HydraulicSim hydSim = new HydraulicSim(net, log);
	    hydSim.simulate(hydFile);
	    HydraulicReader hydReader = new HydraulicReader(
		    new RandomAccessFile(hydFile, "r"));

	    PropertiesMap pMap = net.getPropertiesMap();
	    FieldsMap fmap = net.getFieldsMap();
	    AwareStep step = hydReader.getStep(0);
	    for (Node node : net.getNodes()) {
		NodeWrapper nw = nodesWrapper.get(node.getId());
		double demand = step.getNodeDemand(0, node, fmap);
		double head = step.getNodeHead(0, node, fmap);
		double pressure = step.getNodePressure(0, node, fmap);
		nw.setDemand(demand);
		nw.setHead(head);
		nw.setPressure(pressure);
		// base demand
		// elevation

	    }

	    for (Link link : net.getLinks()) {
		LinkWrapper lw = linksWrapper.get(link.getId());
		// lenght
		// diameter
		// roughness
		double flow = Math.abs(step.getLinkFlow(0, link, fmap));
		double velocity = Math.abs(step.getLinkVelocity(0, link, fmap));
		double unitheadloss = step.getLinkHeadLoss(0, link, fmap);
		double frictionFactor = step.getLinkFriction(0, link, fmap);
		lw.setFlow(flow);
		lw.setVelocity(velocity);
		lw.setUnitHeadLoss(unitheadloss);
		lw.setFrictionFactor(frictionFactor);
	    }
	    deleteHydFile(hydFile);
	} catch (IOException e) {
	    throw new ExternalError(e);

	} catch (ENException e) {
	    throw new InvalidNetworkError();
	}
    }

    private void deleteHydFile(File hydFile) {
	if (hydFile != null) {
	    hydFile.delete();
	}
    }

    public Map<String, LinkWrapper> getLinks() {
	return linksWrapper;
    }

    public Map<String, NodeWrapper> getNodes() {
	return nodesWrapper;
    }

}
