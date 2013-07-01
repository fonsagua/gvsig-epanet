package es.udc.cartolab.gvsig.fonsagua.epanet;

import java.util.logging.Logger;

import org.addition.epanet.network.Network;
import org.addition.epanet.network.Network.FileType;
import org.addition.epanet.network.PropertiesMap;
import org.addition.epanet.network.PropertiesMap.FlowUnitsType;
import org.addition.epanet.network.PropertiesMap.FormType;
import org.addition.epanet.network.PropertiesMap.ReportFlag;
import org.addition.epanet.network.PropertiesMap.StatFlag;
import org.addition.epanet.network.io.input.InputParser;
import org.addition.epanet.network.structures.Control;
import org.addition.epanet.network.structures.Control.ControlType;
import org.addition.epanet.network.structures.Curve;
import org.addition.epanet.network.structures.Curve.CurveType;
import org.addition.epanet.network.structures.Demand;
import org.addition.epanet.network.structures.Link;
import org.addition.epanet.network.structures.Link.LinkType;
import org.addition.epanet.network.structures.Link.StatType;
import org.addition.epanet.network.structures.Node;
import org.addition.epanet.network.structures.Pattern;
import org.addition.epanet.network.structures.Point;
import org.addition.epanet.network.structures.Pump;
import org.addition.epanet.network.structures.Tank;
import org.addition.epanet.util.ENException;

//Los getXX pueden seguir devolviendo el valor para poder referenciar
//el objeto en lugar del ID
//
//Hay que conseguir una instancia de NullParser y llamar a convert
public class NetworkBuilder {

    private Network net;
    private InputParser parser;

    public NetworkBuilder() {
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

    public void getNode(String id, double x, double y, int elevation,
	    int baseDemand) {
	// TODO: Probablemente habría que hacer dos métodos, 1 para los nodos
	// con demanda y otro para los que no tiene demanda. Chequear
	// parseJunction
	Node node = new Node();
	node.setPosition(new Point(x, y));
	node.setElevation(elevation);
	Demand demand = new Demand(baseDemand, null);
	node.getDemand().add(demand);
	node.setInitDemand(baseDemand);
	node.setId(id);
	net.addJunction(node.getId(), node);
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
	tank.setMixModel(Tank.MixType.MIX1); // debería estar en initTanks
	net.addTank(tank.getId(), tank);

    }

    public void getReservoir(String id, double x, double y, int totalHead) {
	Tank tank = new Tank();
	tank.setId(id);
	tank.setPosition(new Point(x, y));
	tank.setElevation(totalHead);
	net.addTank(tank.getId(), tank);
    }

    public void getPipe(String id, String startNode, String endNode,
	    double len, double diameter, double roughness) {
	// TODO: MinorLoss
	Link link = new Link();
	link.setId(id);
	link.setFirst(net.getNode(startNode));
	link.setSecond(net.getNode(endNode));
	link.setLenght(len);
	link.setDiameter(diameter);
	link.setRoughness(roughness);
	link.setStatus(StatType.OPEN);
	link.setType(LinkType.PIPE);
	net.addPipe(link.getId(), link);
    }

    public void getPump(String id, String startNode, String endNode,
	    String headCurve) {
	Pump pump = new Pump();
	pump.setId(id);
	pump.setFirst(net.getNode(startNode));
	pump.setSecond(net.getNode(endNode));
	pump.setHcurve(net.getCurve(headCurve));

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

    public void prepare() throws ENException {
	parser.parse(net, null);
    }

}
