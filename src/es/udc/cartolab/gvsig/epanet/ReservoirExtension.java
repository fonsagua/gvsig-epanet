package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;

public class ReservoirExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_reservoirCad";
	layername = "reservoirs";
	iconName = "reservoir";
	tool = new PointCADTool();
	super.initialize();
    }

}
