package es.udc.cartolab.gvsig.epanet;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.EditionManager;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrAnnotation;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public class JunctionExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	layername = Preferences.getLayerNames().getJunctions();
	iconName = "junction";
	tool = new PointCADTool();
	super.initialize();

	NTEndGeometryListener listener = new NTEndGeometryListener();
	CADListenerManager.addEndGeometryListener("epanet-listener", listener);
    }

    protected class NTEndGeometryListener implements EndGeometryListener {

	@Override
	public void endGeometry(FLayer layer, String cadToolKey) {
	    View vista = (View) PluginServices.getMDIManager()
		    .getActiveWindow();
	    EditionManager edMan = CADExtension.getEditionManager();
	    vista.getMapControl().getCanceldraw().setCanceled(true);
	    if (layer instanceof FLyrVect) {
		FLyrVect lv = (FLyrVect) layer;
		VectorialLayerEdited lyrEd = (VectorialLayerEdited) edMan
			.getActiveLayerEdited();
		try {
		    lv.getRecordset().removeSelectionListener(lyrEd);
		} catch (ReadDriverException e) {
		    NotificationManager
			    .addError("Remove Selection Listener", e);
		}
		try {
		    new ToggleEditing().stopEditing(layer, false);
		    lv.removeLayerListener(edMan);
		    if (lv instanceof FLyrAnnotation) {
			FLyrAnnotation lva = (FLyrAnnotation) lv;
			lva.setMapping(lva.getMapping());
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    vista.getMapControl().setTool("zoomIn");
	    vista.hideConsole();
	    vista.repaintMap();
	    CADExtension.clearView();
	    PluginServices.getMainFrame().enableControls();
	}
    }
}
