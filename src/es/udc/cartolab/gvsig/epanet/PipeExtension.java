package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.PipeCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class PipeExtension extends AbstractCADExtension {

    private static boolean externalEnability;

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

    public static void setExternalEnability(boolean validAlternative) {
	externalEnability = validAlternative;
    }

    @Override
    public boolean isEnabled() {
	if (externalEnability) {
	    return super.isEnabled();
	}
	return false;
    }
}
