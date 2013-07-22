package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;

import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class PumpExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	layername = Preferences.getLayerNames().getPumps();
	iconName = "pump";
	tool = new PointCADTool();
	super.initialize();
    }

}
