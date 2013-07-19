package es.udc.cartolab.gvsig.epanet.config;

import java.util.HashSet;
import java.util.Set;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class Preferences {

    static String[] pointLayerNames = { "junctions", "pumps", "reservoirs",
	    "tanks", "valves" };

    public static JunctionFieldNames getJunctionFieldNames() {
	JunctionFieldNames names = new JunctionFieldNames();
	names.setElevation("elevation");
	names.setBaseDemand("basedemand");
	names.setPressure("pressure");
	names.setHead("head");
	names.setDemand("demand");

	return names;
    }

    public static TankFieldNames getTankFieldNames() {
	TankFieldNames names = new TankFieldNames();
	names.setElevation("elevation");
	names.setInitLevel("initlevel");
	names.setMinLevel("minlevel");
	names.setMaxLevel("maxlevel");
	names.setDiameter("diameter");
	names.setPressure("pressure");
	names.setHead("head");
	names.setDemand("demand");
	return names;
    }

    public static ReservoirFieldNames getReservoirFieldNames() {
	ReservoirFieldNames names = new ReservoirFieldNames();
	names.setTotalHead("totalhead");
	names.setPressure("pressure");
	names.setHead("head");
	names.setDemand("demand");
	return names;
    }

    public static PipeFieldNames getPipeFieldNames() {
	PipeFieldNames names = new PipeFieldNames();
	names.setDiameter("diameter");
	names.setRoughness("roughness");
	names.setFlow("flow");
	names.setVelocity("velocity");
	names.setUnitHeadLoss("uhloss");
	names.setFrictionFactor("fricfactor");
	return names;
    }

    public static PumpFieldNames getPumpFieldNames() {
	PumpFieldNames names = new PumpFieldNames();
	names.setElevation("elevation");
	names.setValue("value");
	names.setFlow("flow");
	names.setVelocity("velocity");
	names.setUnitHeadLoss("uhloss");
	names.setFrictionFactor("fricfactor");
	return names;
    }

    public static ValveFieldNames getValveFieldNames() {
	ValveFieldNames names = new ValveFieldNames();
	names.setElevation("elevation");
	names.setDiameter("diameter");
	names.setFlow("flow");
	names.setVelocity("velocity");
	names.setUnitHeadLoss("uhloss");
	names.setFrictionFactor("fricfactor");
	return names;
    }

    public static Set<FLyrVect> getPointLayers() {
	Set<FLyrVect> pointLayers = new HashSet<FLyrVect>();
	FLayers layers = ((View) PluginServices.getMDIManager()
		.getActiveWindow()).getMapControl().getMapContext().getLayers();
	FLayer layer;
	for (String name : pointLayerNames) {
	    layer = layers.getLayer(name);
	    if (layer instanceof FLyrVect) {
		pointLayers.add((FLyrVect) layers.getLayer(name));
	    }
	}
	return pointLayers;
    }

}
