package es.udc.cartolab.gvsig.epanet;

import java.io.File;

import com.iver.andami.PluginServices;

import es.udc.cartolab.gvsig.epanet.config.Preferences;

public class RunExtension extends AbstractExtension {

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
	Run foo = new Run();
	foo.execute();
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
