package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;

public class ValveExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_valveCad";
	layername = "valves";
	iconName = "valve";
	tool = new PointCADTool();
	super.initialize();
    }
}
