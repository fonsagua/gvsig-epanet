package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.PipeCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class PipeExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	iconName = "pipe";
	tool = new PipeCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getPipes();
	super.postInitialize();
    }

}
