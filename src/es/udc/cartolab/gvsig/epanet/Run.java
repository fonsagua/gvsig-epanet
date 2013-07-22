package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.fmap.layers.FLayers;

import es.udc.cartolab.gvsig.epanet.network.LayerParser;

public class Run {

    public void execute(FLayers fLayers) {
	LayerParser layerParser = new LayerParser();
	layerParser.add(fLayers);
	layerParser.hydraulicSim();

    }

}
