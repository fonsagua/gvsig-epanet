package es.udc.cartolab.gvsig.epanet;

import java.awt.Component;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayers;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.network.LayerParser;
import es.udc.cartolab.gvsig.epanet.structures.validations.Warning;

public class RunExtension extends AbstractExtension {

    private static boolean externalEnability;

    @Override
    public void initialize() {
	String baseformpath = PluginServices.getPluginServices(this)
		.getPluginDirectory().getAbsoluteFile()
		+ File.separator
		+ "lib"
		+ File.separator
		+ "BaseformEpaNetLib-1.0.jar";
	Preferences.setBaseformPath(baseformpath);

	registerIcon("run_epanet");

    }

    @Override
    public void execute(String actionCommand) {
	try {
	    PluginServices.getMDIManager().setWaitCursor();
	    LayerParser layerParser = new LayerParser();
	    layerParser.setNodeCheckers(Preferences.getNodeCheckers());
	    layerParser.setLinkCheckers(Preferences.getLinkCheckers());
	    layerParser.setNodesCheckers(Preferences.getNodesCheckers());
	    final FLayers layers = getView().getMapControl().getMapContext()
		    .getLayers();
	    layerParser.add(layers);

	    layerParser.hydraulicSim();

	    List<Warning> simWarnings = layerParser.getSimWarnings();
	    if (!simWarnings.isEmpty()) {
		StringBuilder sb = new StringBuilder();
		for (Warning str : simWarnings) {
		    sb.append(str.getMsg());
		    sb.append("\n");
		}

		JOptionPane.showMessageDialog(
			(Component) PluginServices.getMainFrame(),
			sb.toString(), "", JOptionPane.WARNING_MESSAGE);
	    }
	} catch (InvalidNetworkError e) {
	    JOptionPane.showMessageDialog(
		    (Component) PluginServices.getMainFrame(), e.getMessage(),
		    "", JOptionPane.ERROR_MESSAGE);
	} finally {
	    PluginServices.getMDIManager().restoreCursor();
	}

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
