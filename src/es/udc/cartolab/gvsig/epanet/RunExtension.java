package es.udc.cartolab.gvsig.epanet;


public class RunExtension extends AbstractExtension {

    @Override
    public void initialize() {
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
