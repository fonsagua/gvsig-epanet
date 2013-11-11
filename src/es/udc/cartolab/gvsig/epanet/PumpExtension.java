package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.epanet.cad.PumpCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class PumpExtension extends AbstractCADExtension {

    private static boolean externalEnability = true;

    @Override
    public void initialize() {
	iconName = "pump";
	tool = new PumpCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getPumps();
	super.postInitialize();
    }

    public static void setExternalEnability(boolean validAlternative) {
	externalEnability = validAlternative;
    }

    @Override
    public void execute(String actionCommand) {

	layersToSnap = Preferences.getPointLayers();
	layersToSnap.add((FLyrVect) getView().getMapControl().getMapContext()
		.getLayers().getLayer("bombeos"));

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
