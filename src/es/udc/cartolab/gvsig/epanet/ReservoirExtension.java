package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.ReservoirCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class ReservoirExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	layername = Preferences.getLayerNames().getReservoirs();
	iconName = "reservoir";
	tool = new ReservoirCADTool();
	super.initialize();
    }

}
