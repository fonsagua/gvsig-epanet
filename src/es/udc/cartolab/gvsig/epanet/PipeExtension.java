package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.PipeCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class PipeExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	layername = Preferences.getLayerNames().getPipes();
	iconName = "pipe";
	tool = new PipeCADTool();
	super.initialize();
    }

}
