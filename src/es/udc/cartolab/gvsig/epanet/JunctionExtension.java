package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;

import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public class JunctionExtension extends AbstractCADExtension {

    @Override
    public void initialize() {
	customTool = "_junctionCad";
	layername = "junctions";
	iconName = "junction";
	tool = new PointCADTool();
	super.initialize();

	NTEndGeometryListener listener = new NTEndGeometryListener();
	CADListenerManager.addEndGeometryListener("epanet-listener", listener);
    }

    protected class NTEndGeometryListener implements EndGeometryListener {

	@Override
	public void endGeometry(FLayer layer, String cadToolKey) {

	    ToggleEditing toggleEditing = new ToggleEditing();
	    try {
		toggleEditing.stopEditing(layer, false);
	    } catch (StopWriterVisitorException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
}
