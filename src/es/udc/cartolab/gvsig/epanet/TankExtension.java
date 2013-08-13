package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.TankCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class TankExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	iconName = "tank";
	tool = new TankCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getTanks();
	super.postInitialize();
    }

}
