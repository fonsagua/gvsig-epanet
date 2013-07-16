package es.udc.cartolab.gvsig.epanet;


public class Run extends AbstractExtension {

    @Override
    public void initialize() {
	registerIcon("run_epanet");

    }

    @Override
    public void execute(String actionCommand) {
	Foo foo = new Foo();
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
