package es.udc.cartolab.gvsig.epanet.cad;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.EditionManager;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.listeners.EndGeometryListener;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public class EpanetEndGeometryListener implements EndGeometryListener {

    @Override
    public void endGeometry(FLayer layer, String cadToolKey) {

	if (cadToolKey.startsWith("_epanet_cadtool_")) {
	    View vista = (View) PluginServices.getMDIManager()
		    .getActiveWindow();
	    EditionManager edMan = CADExtension.getEditionManager();
	    vista.getMapControl().getCanceldraw().setCanceled(true);

	    FLyrVect lv = (FLyrVect) layer;

	    try {
		new ToggleEditing().stopEditing(layer, false);
	    } catch (StopWriterVisitorException e) {
		try {
		    new ToggleEditing().stopEditing(layer, true);
		} catch (StopWriterVisitorException e1) {

		}
		throw new ExternalError(e);
	    }

	    lv.removeLayerListener(edMan);

	    vista.getMapControl().setTool("zoomIn");
	    vista.hideConsole();
	    // vista.repaintMap();
	    CADExtension.clearView();
	    PluginServices.getMainFrame().enableControls();
	}
    }
}