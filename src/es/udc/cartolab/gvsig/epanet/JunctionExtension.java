package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.listeners.CADListenerManager;

import es.udc.cartolab.gvsig.epanet.cad.EpanetEndGeometryListener;
import es.udc.cartolab.gvsig.epanet.cad.JunctionCADTool;
import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class JunctionExtension extends AbstractCADExtension {

    private static boolean externalEnability;

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

	EpanetEndGeometryListener listener = new EpanetEndGeometryListener();
	CADListenerManager.addEndGeometryListener("epanet-listener", listener);
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
