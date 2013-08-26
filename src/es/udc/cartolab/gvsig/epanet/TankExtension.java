package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.TankCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class TankExtension extends AbstractCADExtension {

    private static boolean externalEnability;

    @Override
    public void initialize() {
	iconName = "tank";
	tool = new TankCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getTanks();
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
