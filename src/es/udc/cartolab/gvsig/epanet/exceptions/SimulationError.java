package es.udc.cartolab.gvsig.epanet.exceptions;

@SuppressWarnings("serial")
public class SimulationError extends RuntimeException {

    public SimulationError(String msg) {
	super(msg);
    }

    public SimulationError(Throwable cause) {
	super(cause);
    }

}
