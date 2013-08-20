package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.SourceCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class SourceExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	iconName = "source";
	tool = new SourceCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getSources();
	super.postInitialize();
    }
}
