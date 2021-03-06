package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;

import es.udc.cartolab.gvsig.epanet.cad.JunctionCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class JunctionExtension extends AbstractCADExtension {

    private static boolean externalEnability = true;

    @Override
    public void initialize() {
	iconName = "junction";
	tool = new JunctionCADTool();
	super.initialize();
    }

    @Override
    public void postInitialize() {
	layername = Preferences.getLayerNames().getJunctions();
	super.postInitialize();

	EndGeometryListener listener = Preferences.getCADListener();
	CADListenerManager.addEndGeometryListener("epanet-listener", listener);
    }

    @Override
    public void execute(String actionCommand) {

	layersToSnap = Preferences.getPointLayers();
	layersToSnap.add((FLyrVect) getView().getMapControl().getMapContext()
		.getLayers().getLayer("tuberias"));

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
