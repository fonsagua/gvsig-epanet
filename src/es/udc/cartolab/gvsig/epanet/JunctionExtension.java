package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.listeners.CADListenerManager;

import es.udc.cartolab.gvsig.epanet.cad.EpanetEndGeometryListener;
import es.udc.cartolab.gvsig.epanet.cad.JunctionCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class JunctionExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	layername = Preferences.getLayerNames().getJunctions();
	iconName = "junction";
	tool = new JunctionCADTool();
	super.initialize();

	EpanetEndGeometryListener listener = new EpanetEndGeometryListener();
	CADListenerManager.addEndGeometryListener("epanet-listener", listener);
    }
}
