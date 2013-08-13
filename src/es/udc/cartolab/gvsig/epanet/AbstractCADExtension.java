package es.udc.cartolab.gvsig.epanet;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.StartEditing;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.tools.InsertionCADTool;
import com.iver.cit.gvsig.project.documents.view.snapping.VectorialLayerSnapping;

import es.udc.cartolab.gvsig.epanet.config.Preferences;

public abstract class AbstractCADExtension extends AbstractExtension {

    protected String layername;
    private String customTool;
    protected String iconName;
    protected InsertionCADTool tool;
    private FLayer layer;
    private StartEditing startEditingExt;

    @Override
    public void initialize() {
	registerIcon(iconName);
	customTool = "_epanet_cadtool_" + iconName;
	CADExtension.addCADTool(customTool, tool);
	startEditingExt = (StartEditing) PluginServices
		.getExtension(StartEditing.class);
    }

    @Override
    public void execute(String actionCommand) {
	// Utils.removeCADButtons();
	activeOnly(layername);

	startEditingExt.startEditing(getView(), (FLyrVect) layer);
	VectorialLayerSnapping snapTo = new VectorialLayerSnapping(
		(FLyrVect) layer);
	snapTo.setSnappers(Preferences.getPointLayers());

	CADExtension.setCADTool(customTool, true);
    }

    protected void activeOnly(String layername) {
	FLayers layers = getView().getMapControl().getMapContext().getLayers();

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
	layer = null;
	if (super.isEnabled()) {
	    layer = getView().getMapControl().getMapContext().getLayers()
		    .getLayer(layername);
	}
	return layer != null;
    }

}
