package es.udc.cartolab.gvsig.epanet;

import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.gui.cad.tools.InsertionCADTool;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

import es.udc.cartolab.gvsig.epanet.cad.SnapUtils;
import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public abstract class AbstractCADExtension extends AbstractExtension {

    protected String layername;
    private String customTool;
    protected String iconName;
    protected InsertionCADTool tool;
    private FLayer layer;
    private boolean snapping = false;;

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
	te.startEditing(layer);

	CADExtension.setCADTool(customTool, true);

	SnapUtils snapUtils = new SnapUtils(Preferences.getPointLayers());
	VectorialLayerEdited vle = (VectorialLayerEdited) CADExtension
		.getEditionManager().getActiveLayerEdited();
	snapUtils.enableSnappersFor(vle);

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
