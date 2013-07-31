package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.ValveCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class ValveExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	layername = Preferences.getLayerNames().getValves();
	iconName = "valve";
	tool = new ValveCADTool();
	super.initialize();
    }
}
