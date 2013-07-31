package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.PumpCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class PumpExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	layername = Preferences.getLayerNames().getPumps();
	iconName = "pump";
	tool = new PumpCADTool();
	super.initialize();
    }

}
