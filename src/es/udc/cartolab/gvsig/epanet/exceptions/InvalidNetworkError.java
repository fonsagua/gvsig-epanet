package es.udc.cartolab.gvsig.epanet.exceptions;

@SuppressWarnings("serial")
public class InvalidNetworkError extends RuntimeException {

    public InvalidNetworkError(String msg) {
	super(msg);
    }

    public InvalidNetworkError(Throwable cause) {
	super(cause);
    }

}
