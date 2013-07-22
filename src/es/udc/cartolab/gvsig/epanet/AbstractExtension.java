package es.udc.cartolab.gvsig.epanet;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public abstract class AbstractExtension extends Extension {

    private View view;

    /**
     * Register the file images/[iconName].png with the name [iconName] in the
     * icon theme
     * 
     */
    protected void registerIcon(String iconName) {
	PluginServices.getIconTheme().registerDefault(
		iconName,
		this.getClass().getClassLoader()
			.getResource("images/" + iconName + ".png"));
    }

    public View getView() {
	return view;
    }

    @Override
    public boolean isEnabled() {
	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();
	boolean isEnabled = false;

	if (iWindow instanceof View) {
	    isEnabled = true;
	    view = (View) iWindow;
	}

	return isEnabled;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
