package es.udc.cartolab.gvsig.epanet.exceptions;

@SuppressWarnings("serial")
public class InvalidNetworkErrorToFix extends RuntimeException {

    public InvalidNetworkErrorToFix(String msg) {
	super(msg);
    }

    public InvalidNetworkErrorToFix(Throwable cause) {
	super(cause);
    }

}
