package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.PipeCADTool;

public class PipeExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_pipeCad";
	layername = "pipes";
	iconName = "pipe";
	tool = new PipeCADTool();
	super.initialize();
    }

}
