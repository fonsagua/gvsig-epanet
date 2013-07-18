package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;

public class PumpExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_pumpCad";
	layername = "pumps";
	iconName = "pump";
	tool = new PointCADTool();
	super.initialize();
    }

}
