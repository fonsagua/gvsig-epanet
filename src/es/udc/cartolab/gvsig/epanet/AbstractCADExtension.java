package es.udc.cartolab.gvsig.epanet;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.tools.InsertionCADTool;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public abstract class AbstractCADExtension extends AbstractExtension {

    protected String layername;
    private String customTool;
    protected String iconName;
    protected InsertionCADTool tool;

    protected MapControl mapControl;

    @Override
    public void initialize() {
	registerIcon(iconName);
	customTool = "_epanet_cadtool_" + layername;
	CADExtension.addCADTool(customTool, tool);
    }

    @Override
    public void execute(String actionCommand) {
	// Utils.removeCADButtons();
	activeOnly(layername);
	CADExtension.initFocus();
	ToggleEditing te = new ToggleEditing();
	te.startEditing(getLayerByName(layername));

	CADExtension.setCADTool(customTool, true);

    }

    protected FLyrVect getLayerByName(String layerName) {
	View view = (View) PluginServices.getMDIManager().getActiveWindow();
	FLayer layer = view.getMapControl().getMapContext().getLayers()
		.getLayer(layerName);
	return (FLyrVect) layer;
    }

    protected void activeOnly(String layername) {
	View view = (View) PluginServices.getMDIManager().getActiveWindow();
	FLayers layers = view.getMapControl().getMapContext().getLayers();

	FLayer layer;
	for (int i = 0; i < layers.getLayersCount(); i++) {
	    layer = layers.getLayer(i);
	    boolean active = false;
	    if (layer.getName().equals(layername)) {
		active = true;
	    }
	    layer.setActive(active);
	}
    }

    @Override
    public boolean isEnabled() {
	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();

	if (iWindow instanceof View) {
	    mapControl = ((View) iWindow).getMapControl();
	    return true;
	}

	return false;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
