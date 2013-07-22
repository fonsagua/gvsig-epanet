package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;

import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class ValveExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_valveCad";
	layername = Preferences.getLayerNames().getValves();
	iconName = "valve";
	tool = new PointCADTool();
	super.initialize();
    }
}
