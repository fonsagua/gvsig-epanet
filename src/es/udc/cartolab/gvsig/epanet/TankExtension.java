package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.epanet.cad.TankCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class TankExtension extends AbstractCADExtension {

    private static boolean externalEnability = true;

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

    @Override
    public void execute(String actionCommand) {

	layersToSnap = Preferences.getPointLayers();
	final FLayers layers = getView().getMapControl().getMapContext()
		.getLayers();
	layersToSnap.add((FLyrVect) layers.getLayer("dep_intermedios"));
	layersToSnap.add((FLyrVect) layers.getLayer("dep_distribucion"));

	super.execute(actionCommand);
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
