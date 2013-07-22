package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;

import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class TankExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_tankCad";
	layername = Preferences.getLayerNames().getTanks();
	iconName = "tank";
	tool = new PointCADTool();
	super.initialize();
    }

}
