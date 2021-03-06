package es.udc.cartolab.gvsig.epanet;

import es.udc.cartolab.gvsig.epanet.cad.ValveCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class ValveExtension extends AbstractCADExtension {

    private static boolean externalEnability = true;

    @Override
    public void initialize() {
	iconName = "valve";
	tool = new ValveCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getValves();
	super.postInitialize();
    }

    public static void setExternalEnability(boolean validAlternative) {
	externalEnability = validAlternative;
    }

    @Override
    public void execute(String actionCommand) {

	layersToSnap = Preferences.getPointLayers();

	super.execute(actionCommand);
    }

    @Override
    public boolean isEnabled() {
	if (externalEnability) {
	    return super.isEnabled();
	}
	return false;
    }
}
