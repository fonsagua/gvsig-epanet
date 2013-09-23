package es.udc.cartolab.gvsig.epanet.exceptions;

@SuppressWarnings("serial")
public class InvalidNetworkError extends Exception {

    private ErrorCode code;

    public InvalidNetworkError(ErrorCode code, String msg) {
	super(msg);
	this.code = code;
    }

    public InvalidNetworkError(ErrorCode code, String msg, Throwable cause) {
	super(msg, cause);
	this.code = code;
    }

}
