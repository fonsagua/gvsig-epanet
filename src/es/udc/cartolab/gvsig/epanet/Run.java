package es.udc.cartolab.gvsig.epanet;

import java.io.File;

import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.epanet.network.LayerParser;

public class Run {

    public void execute() {
	FLyrVect pumps = getLayerByName("pumps");
	FLyrVect valves = getLayerByName("valves");
	FLyrVect pipes = getLayerByName("pipes");
	FLyrVect junctions = getLayerByName("junctions");
	FLyrVect reservoirs = getLayerByName("reservoirs");
	FLyrVect tanks = getLayerByName("tanks");

	LayerParser layerParser = new LayerParser();
	try {
	    layerParser.addReservoirs(reservoirs);
	    layerParser.addValves(valves);
	    layerParser.addPumps(pumps);
	    layerParser.addJunctions(junctions);
	    layerParser.addTanks(tanks);
	    layerParser.addPipes(pipes);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	layerParser.createInpFile(new File(Launcher.getAppHomeDir()
		+ File.separator + "foo.inp"));

    }

    private FLyrVect getLayerByName(String layerName) {
	View view = (View) PluginServices.getMDIManager().getActiveWindow();
	FLyrVect layer = (FLyrVect) view.getMapControl().getMapContext()
		.getLayers().getLayer(layerName);
	return layer;
    }

}
