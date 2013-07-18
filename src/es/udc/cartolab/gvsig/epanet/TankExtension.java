package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;

public class TankExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_tankCad";
	layername = "tanks";
	iconName = "tank";
	tool = new PointCADTool();
	super.initialize();
    }

}
