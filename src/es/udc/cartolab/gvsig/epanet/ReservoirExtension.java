package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;

import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class ReservoirExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_reservoirCad";
	layername = Preferences.getLayerNames().getReservoirs();
	iconName = "reservoir";
	tool = new PointCADTool();
	super.initialize();
    }

}
