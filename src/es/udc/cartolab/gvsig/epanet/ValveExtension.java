package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.ValveCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class ValveExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	iconName = "valve";
	tool = new ValveCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getValves();
	super.postInitialize();
    }
}
