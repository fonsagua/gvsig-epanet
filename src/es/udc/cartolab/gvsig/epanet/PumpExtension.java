package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.PumpCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class PumpExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	iconName = "pump";
	tool = new PumpCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getPumps();
	super.postInitialize();
    }

}
