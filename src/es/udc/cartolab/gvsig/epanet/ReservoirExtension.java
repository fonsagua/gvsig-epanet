package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.epanet.cad.ReservoirCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class ReservoirExtension extends AbstractCADExtension {

    private static boolean externalEnability;

    @Override
    public void initialize() {
	iconName = "reservoir";
	tool = new ReservoirCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getReservoirs();
	super.postInitialize();
    }

    @Override
    public void execute(String actionCommand) {

	layersToSnap = Preferences.getPointLayers();
	layersToSnap.add((FLyrVect) getView().getMapControl().getMapContext()
		.getLayers().getLayer("fuentes"));

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
