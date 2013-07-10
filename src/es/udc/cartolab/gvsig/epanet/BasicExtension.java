package es.udc.cartolab.gvsig.epanet;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

public abstract class BasicExtension extends Extension {

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

}
