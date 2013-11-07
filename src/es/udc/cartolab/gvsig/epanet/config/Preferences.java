package es.udc.cartolab.gvsig.epanet.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.epanet.structures.validations.FlowSense;
import es.udc.cartolab.gvsig.epanet.structures.validations.LinkChecker;
import es.udc.cartolab.gvsig.epanet.structures.validations.NegativePressure;
import es.udc.cartolab.gvsig.epanet.structures.validations.NodeChecker;
import es.udc.cartolab.gvsig.epanet.structures.validations.NodesChecker;
import es.udc.cartolab.gvsig.epanet.structures.validations.ReservoirDemand;
import es.udc.cartolab.gvsig.epanet.structures.validations.SourcePressure;
import es.udc.cartolab.gvsig.epanet.structures.validations.ValveLosts;

public class Preferences {

    private static String baseformpath;
    private static JunctionFieldNames junctionFieldNames;
    private static TankFieldNames tankFieldNames;
    private static ReservoirFieldNames reservoirFieldNames;
    private static PipeFieldNames pipeFieldNames;
    private static PumpFieldNames pumpFieldNames;
    private static ValveFieldNames valveFieldNames;
    private static LayerNames layerNames;
    private static JunctionFieldNames sourceFieldNames;
    private static Map<String, NodeChecker> nodeCheckers;
    private static Map<String, LinkChecker> linkCheckers;
    private static HashMap<String, NodesChecker> nodesCheckers;

    public static LayerNames getLayerNames() {
	if (layerNames == null) {
	    layerNames = new LayerNames();
	}
	return layerNames;
    }

    public static JunctionFieldNames getJunctionFieldNames() {
	if (junctionFieldNames == null) {
	    junctionFieldNames = new JunctionFieldNames();
	}

	return junctionFieldNames;
    }

    public static TankFieldNames getTankFieldNames() {
	if (tankFieldNames == null) {
	    tankFieldNames = new TankFieldNames();
	}
	return tankFieldNames;
    }

    public static ReservoirFieldNames getReservoirFieldNames() {
	if (reservoirFieldNames == null) {
	    reservoirFieldNames = new ReservoirFieldNames();
	}
	return reservoirFieldNames;
    }

    public static PipeFieldNames getPipeFieldNames() {
	if (pipeFieldNames == null) {
	    pipeFieldNames = new PipeFieldNames();
	}
	return pipeFieldNames;
    }

    public static PumpFieldNames getPumpFieldNames() {
	if (pumpFieldNames == null) {
	    pumpFieldNames = new PumpFieldNames();
	}
	return pumpFieldNames;
    }

    public static ValveFieldNames getValveFieldNames() {
	if (valveFieldNames == null) {
	    valveFieldNames = new ValveFieldNames();
	}
	return valveFieldNames;
    }

    public static JunctionFieldNames getSourceFieldNames() {
	if (sourceFieldNames == null) {
	    sourceFieldNames = new JunctionFieldNames();
	}
	return sourceFieldNames;
    }

    public static Collection<FLyrVect> getPointLayers() {
	Set<FLyrVect> pointLayers = new HashSet<FLyrVect>();
	FLayers layers = ((View) PluginServices.getMDIManager()
		.getActiveWindow()).getMapControl().getMapContext().getLayers();
	FLayer layer;

	LayerNames layerNames = getLayerNames();
	String[] pointLayerNames = { layerNames.getJunctions(),
		layerNames.getTanks(), layerNames.getReservoirs(),
		layerNames.getPumps(), layerNames.getValves(),
		layerNames.getSources() };

	for (String name : pointLayerNames) {
	    layer = layers.getLayer(name);
	    if (layer instanceof FLyrVect) {
		pointLayers.add((FLyrVect) layers.getLayer(name));
	    }
	}
	return pointLayers;
    }

    public static String getBaseformPath() {
	return baseformpath;
    }

    public static void setBaseformPath(String path) {
	baseformpath = path;
    }

    public static String getBaseformpath() {
	return baseformpath;
    }

    public static void setBaseformpath(String baseformpath) {
	Preferences.baseformpath = baseformpath;
    }

    public static void setJunctionFieldNames(
	    JunctionFieldNames junctionFieldNames) {
	Preferences.junctionFieldNames = junctionFieldNames;
    }

    public static void setTankFieldNames(TankFieldNames tankFieldNames) {
	Preferences.tankFieldNames = tankFieldNames;
    }

    public static void setReservoirFieldNames(
	    ReservoirFieldNames reservoirFieldNames) {
	Preferences.reservoirFieldNames = reservoirFieldNames;
    }

    public static void setPipeFieldNames(PipeFieldNames pipeFieldNames) {
	Preferences.pipeFieldNames = pipeFieldNames;
    }

    public static void setPumpFieldNames(PumpFieldNames pumpFieldNames) {
	Preferences.pumpFieldNames = pumpFieldNames;
    }

    public static void setValveFieldNames(ValveFieldNames valveFieldNames) {
	Preferences.valveFieldNames = valveFieldNames;
    }

    public static void setSourceFieldNames(JunctionFieldNames sourceFieldNames) {
	Preferences.sourceFieldNames = sourceFieldNames;
    }

    public static void setLayerNames(LayerNames layerNames) {
	Preferences.layerNames = layerNames;
    }

    public static Map<String, NodeChecker> getNodeCheckers() {
	if (nodeCheckers == null) {
	    nodeCheckers = new HashMap<String, NodeChecker>();
	    nodeCheckers.put(NegativePressure.class.getName(),
		    new NegativePressure());
	    nodeCheckers.put(SourcePressure.class.getName(),
		    new SourcePressure());
	    nodeCheckers.put(ReservoirDemand.class.getName(),
		    new ReservoirDemand());
	}
	return nodeCheckers;
    }

    public static Map<String, LinkChecker> getLinkCheckers() {
	if (linkCheckers == null) {
	    linkCheckers = new HashMap<String, LinkChecker>();
	    linkCheckers.put(FlowSense.class.getName(), new FlowSense());
	    linkCheckers.put(ValveLosts.class.getName(), new ValveLosts());
	}
	return linkCheckers;
    }

    public static Map<String, NodesChecker> getNodesCheckers() {
	if (nodesCheckers == null) {
	    nodesCheckers = new HashMap<String, NodesChecker>();
	}
	return nodesCheckers;
    }

}
